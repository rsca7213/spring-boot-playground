-- Generic multimedia data for development environment
INSERT INTO multimedia
    (id, uri, mime_type, file_name)
VALUES
    (
        gen_random_uuid(),
        'generic.png',
        'image/png',
        'generic.png'
    );