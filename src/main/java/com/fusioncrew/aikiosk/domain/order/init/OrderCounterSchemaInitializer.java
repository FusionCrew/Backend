package com.fusioncrew.aikiosk.domain.order.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCounterSchemaInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public OrderCounterSchemaInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        // Keep schema creation idempotent (safe to run multiple times).
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS order_counters (
                    id BIGSERIAL PRIMARY KEY,
                    counter_date DATE NOT NULL,
                    store_id VARCHAR(80) NOT NULL,
                    kiosk_id VARCHAR(80) NOT NULL,
                    current_value INTEGER NOT NULL DEFAULT 0
                );
                """);

        jdbcTemplate.execute("""
                CREATE UNIQUE INDEX IF NOT EXISTS ux_order_counters_date_store_kiosk
                ON order_counters(counter_date, store_id, kiosk_id);
                """);

        // orders.order_number may not exist in old DBs.
        jdbcTemplate.execute("ALTER TABLE orders ADD COLUMN IF NOT EXISTS order_number INTEGER;");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS ix_orders_order_number ON orders(order_number);");
    }
}

