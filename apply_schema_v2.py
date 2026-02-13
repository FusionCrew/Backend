import psycopg2
import psycopg2.extras

# Copying config exactly as migrate_db.py (which worked)
NEW_DB_CONFIG = {
    "host": "db.rxfdeaizxpybdpffxbxq.supabase.co",
    "port": "5432",
    "database": "postgres",
    "user": "postgres",
    # Password set in main
}

SQL_SCRIPT = """
DROP TABLE IF EXISTS feedback_tags CASCADE;
DROP TABLE IF EXISTS feedbacks CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS menu_ingredient_links CASCADE;
DROP TABLE IF EXISTS menu_items CASCADE;
DROP TABLE IF EXISTS ingredients CASCADE;
DROP TABLE IF EXISTS stocks CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS tickets CASCADE;
DROP TABLE IF EXISTS session_event CASCADE;
DROP TABLE IF EXISTS kiosk_session CASCADE;
DROP TABLE IF EXISTS admin_users CASCADE;

CREATE TABLE admin_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    refresh_token VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    ingredient_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    allergy_tag VARCHAR(255),
    calories INTEGER NOT NULL,
    extra_price INTEGER NOT NULL,
    protein INTEGER NOT NULL DEFAULT 0,
    sodium INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE stocks (
    id BIGSERIAL PRIMARY KEY,
    ingredient_id VARCHAR(80) NOT NULL UNIQUE,
    quantity INTEGER NOT NULL,
    out_of_stock BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    menu_item_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    name_en VARCHAR(255),
    price INTEGER NOT NULL,
    hidden BOOLEAN NOT NULL DEFAULT FALSE,
    category_id VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    description VARCHAR(500)
);

CREATE TABLE menu_ingredient_links (
    menu_id BIGINT NOT NULL REFERENCES menu_items(id),
    ingredient_id BIGINT NOT NULL REFERENCES ingredients(id),
    PRIMARY KEY (menu_id, ingredient_id)
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(80) NOT NULL,
    order_id VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(255) NOT NULL,
    status_update_note VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    menu_item_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    options_json TEXT,
    id_order BIGINT REFERENCES orders(id)
);

CREATE TABLE feedbacks (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL,
    rating INTEGER NOT NULL,
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE feedback_tags (
    feedback_id BIGINT NOT NULL REFERENCES feedbacks(id),
    tag VARCHAR(255)
);

CREATE TABLE kiosk_session (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL UNIQUE,
    language VARCHAR(255) NOT NULL,
    large_text BOOLEAN NOT NULL,
    high_contrast BOOLEAN NOT NULL,
    voice_guidance BOOLEAN NOT NULL,
    input_mode VARCHAR(255) NOT NULL,
    kiosk_id VARCHAR(255) NOT NULL,
    app_version VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    ended_at TIMESTAMP WITH TIME ZONE,
    end_reason VARCHAR(255)
);

CREATE TABLE session_event (
    id BIGSERIAL PRIMARY KEY,
    kiosk_session_id BIGINT NOT NULL REFERENCES kiosk_session(id),
    type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    method VARCHAR(255) NOT NULL,
    mock BOOLEAN NOT NULL,
    return_url VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    pg_tid VARCHAR(255),
    pg_provider VARCHAR(255),
    refunded_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    approved_at TIMESTAMP WITH TIME ZONE,
    buyer_type VARCHAR(255),
    kiosk_id VARCHAR(255),
    failure_reason VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    payment_id VARCHAR(255) NOT NULL,
    number INTEGER NOT NULL,
    status VARCHAR(255) NOT NULL,
    priority VARCHAR(255) NOT NULL DEFAULT 'NORMAL',
    called_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
"""

def main():
    print("--- Applying Custom Schema ---")
    NEW_DB_CONFIG["password"] = "TEST"
    print(f"DEBUG Config: {NEW_DB_CONFIG}")
    
    conn_str = f"host={NEW_DB_CONFIG['host']} port={NEW_DB_CONFIG['port']} dbname={NEW_DB_CONFIG['database']} user={NEW_DB_CONFIG['user']} password={NEW_DB_CONFIG['password']} sslmode=disable"
    # print(f"DEBUG Conn Str: {conn_str}") # Don't print password normally, but for debug we need to see if chars are weird
    
    try:
        conn = psycopg2.connect(conn_str)
        conn.set_client_encoding('UTF8')
        cur = conn.cursor()
        print("Connected!")
        
        cur.execute("SET client_min_messages TO WARNING;")
        
        # Execute statement by statement? Or all at once?
        # Trying all at once
        cur.execute(SQL_SCRIPT)
        conn.commit()
        
        print("Schema Applied Successfully.")
        cur.close()
        conn.close()
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    main()
