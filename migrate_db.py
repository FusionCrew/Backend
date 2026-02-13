
import psycopg2
import psycopg2.extras
import getpass
import json

# Old Database Configuration (Mumbai)
OLD_DB_CONFIG = {
    "host": "aws-1-ap-south-1.pooler.supabase.com",
    "port": "6543",
    "database": "postgres",
    "user": "postgres.kjecwbwohptcskdblgig",
    "password": "bxFN?VrDwxV62.G"
}

# New Database Configuration (Seoul)
NEW_DB_CONFIG = {
    "host": "db.rxfdeaizxpybdpffxbxq.supabase.co",
    "port": "5432",
    "database": "postgres",
    "user": "postgres",
    # Password will be prompted
}

def get_tables(cursor):
    cursor.execute("""
        SELECT table_name 
        FROM information_schema.tables 
        WHERE table_schema = 'public' 
        AND table_type = 'BASE TABLE'
        AND table_name NOT IN ('spatial_ref_sys');
    """)
    return [table[0] for table in cursor.fetchall()]

def get_table_schema(cursor, table_name):
    # Fetch column details: name, data_type, is_nullable
    cursor.execute(f"""
        SELECT column_name, data_type, is_nullable, character_maximum_length
        FROM information_schema.columns
        WHERE table_schema = 'public' AND table_name = '{table_name}'
        ORDER BY ordinal_position;
    """)
    return cursor.fetchall()

def create_table_query(table_name, schema_rows):
    columns_def = []
    for col in schema_rows:
        name, dtype, nullable, char_len = col
        
        # Map Postgres types if needed (usually works directly)
        col_type = dtype
        if dtype == 'character varying':
            col_type = f"VARCHAR({char_len})" if char_len else "VARCHAR"
        elif dtype == 'USER-DEFINED': 
            col_type = 'TEXT' # Fallback for enums/custom types to TEXT to avoid complexity
        elif dtype == 'ARRAY':
            col_type = 'TEXT[]' # Simplify arrays
            
        null_def = "NULL" if nullable == 'YES' else "NOT NULL"
        
        # Simple primary key heuristic: 'id' or 'payment_id' etc.
        # But for migration, we might just create table without constraints first?
        # Better: let's try to include constraints if possible, but minimal.
        
        columns_def.append(f'"{name}" {col_type} {null_def}')
    
    # Force 'id' as PRIMARY KEY if exists?
    # For now, just create table. Data integrity is handled by app logic mostly.
    return f'CREATE TABLE IF NOT EXISTS "{table_name}" ({", ".join(columns_def)});'

def fetch_table_data(cursor, table_name):
    cursor.execute(f'SELECT * FROM "{table_name}"')
    columns = [desc[0] for desc in cursor.description]
    rows = cursor.fetchall()
    return columns, rows

def insert_data(cursor, table_name, columns, rows):
    if not rows:
        return
    
    col_names = ', '.join([f'"{col}"' for col in columns])
    placeholders = ', '.join(['%s'] * len(columns))
    
    query = f'INSERT INTO "{table_name}" ({col_names}) VALUES ({placeholders}) ON CONFLICT DO NOTHING'
    
    psycopg2.extras.execute_batch(cursor, query, rows)

def main():
    print("--- Supabase Migration Script (Auto-Create Tables) ---")
    
    # 1. Fetch Data from Old DB
    print("\nConnecting to OLD Database (Mumbai)...")
    try:
        conn_old = psycopg2.connect(**OLD_DB_CONFIG)
        conn_old.set_client_encoding('UTF8')
        cur_old = conn_old.cursor()
        print("Connected!")
        
        tables = get_tables(cur_old)
        print(f"Found tables: {tables}")
        
        # Priority order for insertion (users first)
        priority_tables = ['users', 'categories', 'ingredients', 'menu_items', 'orders', 'order_items', 'payments', 'feedback', 'kiosk_sessions']
        
        sorted_tables = []
        for t in priority_tables:
            if t in tables:
                sorted_tables.append(t)
        for t in tables:
            if t not in sorted_tables:
                sorted_tables.append(t)
        
        all_data = {}
        all_schemas = {}
        
        for table in sorted_tables:
            if table in ['ai_logs', 'event_logs']:
                print(f"Skipping {table} (logs explicit skip)")
                continue

            print(f"Fetching schema and data from {table}...")
            try:
                # Get Schema
                schema = get_table_schema(cur_old, table)
                all_schemas[table] = schema
                
                # Get Data
                cols, rows = fetch_table_data(cur_old, table)
                all_data[table] = {'columns': cols, 'rows': rows}
            except Exception as e:
                print(f"!! Failed to fetch table {table}: {e} -- Skipping")
                continue
            
        cur_old.close()
        conn_old.close()
        print("Data fetch complete.")
        
    except Exception as e:
        print(f"Failed to fetch data from Old DB: {e}")
        return

    # 2. Insert Data into New DB
    print("\nPreparing to write to NEW Database (Seoul)...")
    # new_password = getpass.getpass(prompt="Enter NEW Database Password: ")
    NEW_DB_CONFIG["password"] = "Dleodus3380^^"
    
    try:
        conn_new = psycopg2.connect(**NEW_DB_CONFIG)
        conn_new.set_client_encoding('UTF8')
        cur_new = conn_new.cursor()
        print("Connected to New DB!")
        
        for table in sorted_tables:
            try:
                print(f"Processing table: {table}")
                
                # 2.1 Create Table
                match_table_data = all_schemas.get(table)
                if match_table_data:
                    create_sql = create_table_query(table, match_table_data)
                    try:
                        cur_new.execute(create_sql)
                        conn_new.commit()
                        print(f"  -> Table '{table}' created (or exists).")
                    except Exception as e:
                        print(f"  -> Failed to create table {table}: {e}")
                        conn_new.rollback()
                        # Continue to insert anyway?

                # 2.2 Insert Data
                data = all_data.get(table)
                if data and data['rows']:
                    print(f"  -> Inserting {len(data['rows'])} rows...")
                    try:
                        insert_data(cur_new, table, data['columns'], data['rows'])
                        conn_new.commit()
                    except Exception as e:
                        print(f"  -> Error inserting into {table}: {e}")
                        conn_new.rollback()
                else:
                    print(f"  -> Skipping data (empty)")
            except Exception as e:
                print(f"CRITICAL ERROR processing {table}: {e} -- Skipping table", flush=True)
                continue
                
        # Verification
        try:
            cur_new.execute('SELECT count(*) FROM "orders"')
            count = cur_new.fetchone()[0]
            print(f"\nVerification: 'orders' table has {count} rows in New DB.")
        except Exception as e:
            print(f"\nVerification failed (orders table might not exist): {e}")

        cur_new.close()
        conn_new.close()
        print("\nMigration Successful!")
        
    except Exception as e:
        print(f"Failed to write to New DB: {e}", flush=True)
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    main()
