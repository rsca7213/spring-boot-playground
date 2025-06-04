-- Generic product data for development environment
INSERT INTO products
    (id, name, description, price, stock_quantity, multimedia_id, category)
VALUES
    (
        gen_random_uuid(),
        'Generic Product 1',
        'This is a generic product description for product 1.',
        19.99,
        100,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'ELECTRONICS'
    ),
    (
        gen_random_uuid(),
        'Generic Product 2',
        'This is a generic product description for product 2.',
        29.99,
        50,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'FOOD'
    ),
    (
        gen_random_uuid(),
        'Generic Product 3',
        'This is a generic product description for product 3.',
        39.99,
        75,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'CLOTHING'
    );