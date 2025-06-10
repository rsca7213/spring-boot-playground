-- Generic product data for development environment
INSERT INTO products
    (id, name, description, price, stock_quantity, multimedia_id, category)
VALUES
    (
        gen_random_uuid(),
        'Burger',
        'A delicious beef burger with cheese and lettuce.',
        19.99,
        100,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'FOOD'
    ),
    (
        gen_random_uuid(),
        'Pizza',
        'A classic margherita pizza with fresh basil.',
        15.99,
        50,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'FOOD'
    ),
    (
        gen_random_uuid(),
        'Soda',
        'A refreshing cola drink.',
        2.99,
        200,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'FOOD'
    ),
    (
        gen_random_uuid(),
        'Salad',
        'A healthy garden salad with a variety of vegetables.',
        9.99,
        75,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'FOOD'
    ),
    (
        gen_random_uuid(),
        'Water Bottle',
        'A 500ml plastic water bottle.',
        1.49,
        300,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'FOOD'
    ),
    (
        gen_random_uuid(),
        'Coffee',
        'A hot cup of coffee to start your day.',
        3.99,
        150,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'FOOD'
    ),
    (
     gen_random_uuid(),
     'T-Shirt',
        'A comfortable cotton t-shirt available in various sizes.',
        12.99,
        200,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'CLOTHING'
    ),
    (
        gen_random_uuid(),
        'Jeans',
        'Classic blue jeans made from durable denim.',
        39.99,
        100,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'CLOTHING'
    ),
    (
        gen_random_uuid(),
        'Sneakers',
        'Stylish and comfortable sneakers for everyday wear.',
        59.99,
        80,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'CLOTHING'
    ),
    (
        gen_random_uuid(),
        'Smart Watch',
        'A sleek wristwatch with a leather strap.',
        89.99,
        50,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'ELECTRONICS'
    ),
    (
        gen_random_uuid(),
        'Laptop',
        'A high-performance laptop with 16GB RAM and 512GB SSD.',
        999.99,
        30,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'ELECTRONICS'
    ),
    (
        gen_random_uuid(),
        'Headphones',
        'Noise-cancelling over-ear headphones for immersive sound.',
        199.99,
        60,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'ELECTRONICS'
    ),
    (
        gen_random_uuid(),
        'Blender',
        'A powerful blender for smoothies and soups.',
        49.99,
        40,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'HOME_APPLIANCES'
    ),
    (
        gen_random_uuid(),
        'Microwave Oven',
        'A compact microwave oven for quick meals.',
        89.99,
        20,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'HOME_APPLIANCES'
    ),
    (
        gen_random_uuid(),
        'Vacuum Cleaner',
        'A lightweight vacuum cleaner for easy cleaning.',
        129.99,
        15,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'HOME_APPLIANCES'
    ),
    (
        gen_random_uuid(),
        'Fiction Book',
        'A bestselling fiction novel by a popular author.',
        14.99,
        100,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'BOOKS'
    ),
    (
        gen_random_uuid(),
        'Cookbook',
        'A collection of delicious recipes for home cooking.',
        24.99,
        80,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'BOOKS'
    ),
    (
        gen_random_uuid(),
        'Children Book',
        'An illustrated book for children with engaging stories.',
        9.99,
        150,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'BOOKS'
    ),
    (
        gen_random_uuid(),
        'Action Figure',
        'A collectible action figure from a popular movie franchise.',
        29.99,
        200,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'TOYS'
    ),
    (
        gen_random_uuid(),
        'Puzzle',
        'A challenging jigsaw puzzle with 1000 pieces.',
        19.99,
        120,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'TOYS'
    ),
    (
        gen_random_uuid(),
        'Board Game',
        'A fun board game for family and friends.',
        34.99,
        90,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'TOYS'
    ),
    (
        gen_random_uuid(),
        'Yoga Mat',
        'A non-slip yoga mat for comfortable workouts.',
        29.99,
        150,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'SPORTS'
    ),
    (
        gen_random_uuid(),
        'Dumbbells',
        'A set of adjustable dumbbells for strength training.',
        49.99,
        80,
        (SELECT id FROM multimedia WHERE uri = 'generic.png'),
        'SPORTS'
    );
