-- PostgreSQL initialization script
-- Auto-runs on first Docker startup

CREATE TABLE IF NOT EXISTS users (
    id           BIGSERIAL PRIMARY KEY,
    email        VARCHAR(255) UNIQUE NOT NULL,
    password     VARCHAR(255) NOT NULL,
    full_name    VARCHAR(255) NOT NULL,
    phone        VARCHAR(20),
    role         VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    enabled      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP DEFAULT NOW(),
    updated_at   TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS orders (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount     NUMERIC(10,2) NOT NULL,
    shipping_address TEXT NOT NULL,
    payment_method   VARCHAR(50),
    tracking_number  VARCHAR(100),
    notes            TEXT,
    created_at       TIMESTAMP DEFAULT NOW(),
    updated_at       TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS order_items (
    id                BIGSERIAL PRIMARY KEY,
    order_id          BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id        VARCHAR(100) NOT NULL,
    product_name      VARCHAR(255) NOT NULL,
    product_image_url TEXT,
    quantity          INT NOT NULL,
    unit_price        NUMERIC(10,2) NOT NULL,
    subtotal          NUMERIC(10,2) NOT NULL
);

-- Seed admin user (password: Admin@1234)
INSERT INTO users (email, password, full_name, role)
VALUES (
  'admin@shopwave.com',
  '$2a$10$YQ7OFGUFqTJwvvtGvVJ5aOXXIq.7RWbD2YD/F8KBwK8bVsPBVROWy',
  'Shop Admin',
  'ADMIN'
) ON CONFLICT (email) DO NOTHING;
