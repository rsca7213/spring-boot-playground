-- Users to be loaded into the database for development purposes
INSERT INTO users
    (id, email, password_hash, first_name, last_name, role_id)
VALUES
    (
        gen_random_uuid(),
        'admin@springbootplayground.com',
        -- dev password: 'Password123*'
        '$2a$12$O7LlzNDeWtZaNTl66NLRFOj/iIXPwmunhhF1QcKZtqYOkw3ttfeCO',
        'John',
        'Doe',
        (SELECT id FROM roles WHERE name = 'ADMIN')
    ),
    (
        gen_random_uuid(),
        'client@springbootplayground.com',
        -- dev password: 'Password123*'
        '$2a$12$O7LlzNDeWtZaNTl66NLRFOj/iIXPwmunhhF1QcKZtqYOkw3ttfeCO',
        'Jane',
        'Doe',
        (SELECT id FROM roles WHERE name = 'CLIENT')
    );