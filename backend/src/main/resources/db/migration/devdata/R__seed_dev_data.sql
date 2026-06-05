-- ==========================================================
-- 1. IDENTITY MODULE: Users
-- ==========================================================
INSERT INTO users (email, password, name, surname1, surname2, created_at)
VALUES
    ('admin@arenic.com', '$2a$12$UU48VkBs6YyRwSgkxqQHxO0VvaIMDJHBcBzo/V0.morPFCjD3NJSW', 'Carlos', 'Alcaraz', 'Admin', NOW()),
    ('player@arenic.com', '$2a$12$V3NHpICo9d5Gx.abEln2wepkIUJiJivBWqN8oU.vqfBsjhOlKQlRu', 'Rafa', 'Nadal', 'User', NOW())
ON CONFLICT (email) DO NOTHING;
-- Note: Using email for conflict check since we aren't providing IDs
-- ==========================================================
-- 2. CLUB MODULE: Locations
-- ==========================================================
INSERT INTO locations (address_line_1, city, zip_code, country_code, timezone)
VALUES
    ('Calle de Serrano, 12',          'Madrid',            '28001', 'ES', 'Europe/Madrid'),
    ('Calle de Alcalá, 45',           'Madrid',            '28014', 'ES', 'Europe/Madrid'),
    ('Calle Gran Vía, 78',            'Madrid',            '28013', 'ES', 'Europe/Madrid'),
    ('Calle de Velázquez, 33',        'Madrid',            '28001', 'ES', 'Europe/Madrid'),
    ('Avinguda de Diagonal, 405',     'Barcelona',         '08006', 'ES', 'Europe/Madrid'),
    ('Carrer de Balmes, 200',         'Barcelona',         '08006', 'ES', 'Europe/Madrid'),
    ('Carrer de Provença, 150',       'Barcelona',         '08008', 'ES', 'Europe/Madrid'),
    ('Calle Larios, 5',               'Málaga',            '29005', 'ES', 'Europe/Madrid'),
    ('Calle San Fernando, 10',        'Sevilla',           '41004', 'ES', 'Europe/Madrid'),
    ('Calle Colón, 22',               'Valencia',          '46004', 'ES', 'Europe/Madrid');

-- ==========================================================
-- 3. CLUB MODULE: Clubs
-- ==========================================================
INSERT INTO clubs (name, location_id, creator_id, is_active, created_at)
VALUES
    (
        'Real Madrid Padel Center',
        (SELECT id FROM locations WHERE address_line_1 = 'Calle de Serrano, 12' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Madrid Sports Club',
        (SELECT id FROM locations WHERE address_line_1 = 'Calle de Alcalá, 45' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Gran Vía Tennis Academy',
        (SELECT id FROM locations WHERE address_line_1 = 'Calle Gran Vía, 78' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Velázquez Padel Club',
        (SELECT id FROM locations WHERE address_line_1 = 'Calle de Velázquez, 33' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Barcelona Tennis Academy',
        (SELECT id FROM locations WHERE address_line_1 = 'Avinguda de Diagonal, 405' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Balmes Padel Center',
        (SELECT id FROM locations WHERE address_line_1 = 'Carrer de Balmes, 200' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Eixample Sports Club',
        (SELECT id FROM locations WHERE address_line_1 = 'Carrer de Provença, 150' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Málaga Padel & Tennis',
        (SELECT id FROM locations WHERE address_line_1 = 'Calle Larios, 5' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Sevilla Racket Club',
        (SELECT id FROM locations WHERE address_line_1 = 'Calle San Fernando, 10' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    ),
    (
        'Valencia Padel Arena',
        (SELECT id FROM locations WHERE address_line_1 = 'Calle Colón, 22' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true, NOW()
    );