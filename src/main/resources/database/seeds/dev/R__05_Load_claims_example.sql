-- Insert example claims
INSERT INTO coverages
    (name)
VALUES
    ('Catastrophic Event'),
    ('Damage to Third Party'),
    ('Damage to Own Property');

-- Insert example policies
INSERT INTO policies
    (balance, expedition_date, expiration_date)
VALUES
    (100000.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 year'),
    (50000.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 year'),
    (200000.00, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 year');

-- Insert example policy coverages
INSERT INTO policies_coverages
    (policy_id, coverage_id, "limit")
VALUES
    (
        (SELECT id FROM policies WHERE balance = 100000.00),
        (SELECT id FROM coverages WHERE name = 'Catastrophic Event'),
        50000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 100000.00),
        (SELECT id FROM coverages WHERE name = 'Damage to Third Party'),
        30000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 100000.00),
        (SELECT id FROM coverages WHERE name = 'Damage to Own Property'),
        20000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 50000.00),
        (SELECT id FROM coverages WHERE name = 'Catastrophic Event'),
        25000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 50000.00),
        (SELECT id FROM coverages WHERE name = 'Damage to Third Party'),
        15000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 50000.00),
        (SELECT id FROM coverages WHERE name = 'Damage to Own Property'),
        10000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 200000.00),
        (SELECT id FROM coverages WHERE name = 'Catastrophic Event'),
        100000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 200000.00),
        (SELECT id FROM coverages WHERE name = 'Damage to Third Party'),
        60000.00
    ),
    (
        (SELECT id FROM policies WHERE balance = 200000.00),
        (SELECT id FROM coverages WHERE name = 'Damage to Own Property'),
        40000.00
    );