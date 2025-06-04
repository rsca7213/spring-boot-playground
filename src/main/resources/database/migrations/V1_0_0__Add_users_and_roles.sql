-- Roles table creation
CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Index for quick lookup by role name
CREATE INDEX idx_roles_name ON roles(name);

-- Users table creation
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    role_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Index for quick lookup by email
CREATE INDEX idx_users_email ON users(email);

-- Index for quick lookup by role_id
CREATE INDEX idx_users_role_id ON users(role_id);

-- Foreign key constraint for Users table role_id associating with Roles table
ALTER TABLE users
ADD CONSTRAINT fk_users_role_id
FOREIGN KEY (role_id) REFERENCES roles(id);