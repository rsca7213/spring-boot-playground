-- Roles that will be loaded into the database
INSERT INTO roles
    (id, name)
VALUES
    (gen_random_uuid(), 'ADMIN'),
    (gen_random_uuid(), 'CLIENT');