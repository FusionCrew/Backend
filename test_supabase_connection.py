import psycopg2
import sys

# New Supabase Database Configuration
DB_CONFIG = {
    "host": "db.rxfdeaizxpybdpffxbxq.supabase.co",
    "port": "5432",
    "database": "postgres",
    "user": "postgres",
    "password": "Dleodus3380^^"
}

def test_connection():
    print("=== Supabase Connection Test ===")
    print(f"Host: {DB_CONFIG['host']}")
    print(f"Port: {DB_CONFIG['port']}")
    print(f"Database: {DB_CONFIG['database']}")
    print(f"User: {DB_CONFIG['user']}")
    print("\nAttempting connection...")
    
    try:
        # Try connection with explicit encoding
        conn = psycopg2.connect(
            host=DB_CONFIG['host'],
            port=DB_CONFIG['port'],
            database=DB_CONFIG['database'],
            user=DB_CONFIG['user'],
            password=DB_CONFIG['password'],
            sslmode='require',
            connect_timeout=10
        )
        
        print("✅ Connection Successful!\n")
        
        # Get database info
        cur = conn.cursor()
        cur.execute("SELECT version();")
        version = cur.fetchone()[0]
        print(f"📊 PostgreSQL Version: {version.split(',')[0]}")
        
        # List tables
        cur.execute("""
            SELECT table_name 
            FROM information_schema.tables 
            WHERE table_schema = 'public' 
            ORDER BY table_name;
        """)
        tables = cur.fetchall()
        
        print(f"\n📋 Tables in database ({len(tables)} found):")
        if tables:
            for table in tables:
                print(f"  - {table[0]}")
        else:
            print("  (No tables found)")
        
        # Test a simple query on one table if exists
        if tables:
            test_table = tables[0][0]
            try:
                cur.execute(f"SELECT COUNT(*) FROM {test_table};")
                count = cur.fetchone()[0]
                print(f"\n✅ Sample query successful: {test_table} has {count} rows")
            except Exception as e:
                print(f"\n⚠️ Could not query {test_table}: {e}")
        
        cur.close()
        conn.close()
        print("\n✅ Connection test completed successfully!")
        return True
        
    except psycopg2.OperationalError as e:
        print(f"❌ Connection Failed (Operational Error)!")
        print(f"Error: {e}")
        return False
    except Exception as e:
        print(f"❌ Connection Failed!")
        print(f"Error type: {type(e).__name__}")
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    success = test_connection()
    sys.exit(0 if success else 1)
