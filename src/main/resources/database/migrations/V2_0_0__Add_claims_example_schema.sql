-- Coverage table creation
CREATE TABLE coverages (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Index for quick lookup by coverage name
CREATE INDEX idx_coverages_name ON coverages(name);

-- Policy table creation
CREATE TABLE policies (
    id SERIAL PRIMARY KEY,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    expedition_date DATE NOT NULL DEFAULT CURRENT_DATE,
    expiration_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Policy Coverage table creation
CREATE TABLE policies_coverages (
    policy_id INT NOT NULL,
    coverage_id INT NOT NULL,
    "limit" DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    PRIMARY KEY (policy_id, coverage_id)
);

-- Foreign key constraint for Policy Coverage table policy_id associating with Policies table
ALTER TABLE policies_coverages
ADD CONSTRAINT fk_policies_coverages_policy_id
FOREIGN KEY (policy_id) REFERENCES policies(id);

-- Foreign key constraint for Policy Coverage table coverage_id associating with Coverages table
ALTER TABLE policies_coverages
ADD CONSTRAINT fk_policies_coverages_coverage_id
FOREIGN KEY (coverage_id) REFERENCES coverages(id);

-- Claims table creation
CREATE TABLE claims (
    id SERIAL PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    claim_date DATE NOT NULL DEFAULT CURRENT_DATE,
    policy_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Index for quick lookup by claim number
CREATE INDEX idx_claims_number ON claims(number);

-- Foreign key constraint for Claims table policy_id associating with Policies table
ALTER TABLE claims
ADD CONSTRAINT fk_claims_policy_id
FOREIGN KEY (policy_id) REFERENCES policies(id);

-- Claim Coverage table creation
CREATE TABLE claims_coverages (
    claim_id INT NOT NULL,
    coverage_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    PRIMARY KEY (claim_id, coverage_id)
);