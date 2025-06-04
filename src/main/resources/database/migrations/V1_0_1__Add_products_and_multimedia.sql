-- Multimedia table creation
CREATE TABLE multimedia (
    id UUID PRIMARY KEY,
    uri VARCHAR(255) NOT NULL UNIQUE,
    mime_type VARCHAR(100) NOT NULL,
    file_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Index for quick lookup by URI
CREATE INDEX idx_multimedia_uri ON multimedia(uri);

-- Product category enum type creation
CREATE TYPE product_category AS ENUM (
    'ELECTRONICS',
    'CLOTHING',
    'HOME_APPLIANCES',
    'BOOKS',
    'TOYS',
    'SPORTS',
    'BEAUTY',
    'HEALTH',
    'AUTOMOTIVE',
    'GARDEN',
    'FOOD'
);

-- Products table creation
CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    category product_category NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    multimedia_id UUID NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Index for quick lookup by name
CREATE INDEX idx_products_name ON products(name);

-- Index for quick lookup by category
CREATE INDEX idx_products_category ON products(category);

-- Foreign key constraint for Products table multimedia_id associating with Multimedia table
ALTER TABLE products
ADD CONSTRAINT fk_products_multimedia_id
FOREIGN KEY (multimedia_id) REFERENCES multimedia(id);