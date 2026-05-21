-- ==========================================================
-- 1. IDENTITY MODULE: Users
-- ==========================================================
INSERT INTO users (email, password, name, surname1, surname2, created_at)
VALUES
    ('admin@arenic.com', '$2a$10$8.UnVuG9HHgffUDAlk8q6uyzREX7fmmqIdzb/xp6pM68L.fV7M.6y', 'Carlos', 'Alcaraz', 'Admin', NOW()),
    ('player@arenic.com', '$2a$10$8.UnVuG9HHgffUDAlk8q6uyzREX7fmmqIdzb/xp6pM68L.fV7M.6y', 'Rafa', 'Nadal', 'User', NOW())
ON CONFLICT (email) DO NOTHING;
-- Note: Using email for conflict check since we aren't providing IDs

-- ==========================================================
-- 2. CLUB MODULE: Locations
-- ==========================================================
INSERT INTO locations (address_line_1, city, zip_code, country_code, timezone)
VALUES
    ('Calle de Serrano, 12', 'Madrid', '28001', 'ES', 'Europe/Madrid'),
    ('Avinguda de Diagonal, 405', 'Barcelona', '08006', 'ES', 'Europe/Madrid');

-- ==========================================================
-- 3. CLUB MODULE: Clubs
-- Using subqueries to find the IDs since we let the DB generate them
-- ==========================================================
INSERT INTO clubs (name, location_id, creator_id, is_active, created_at)
VALUES
    (
        'Real Madrid Padel Center',
        (SELECT id FROM locations WHERE city = 'Madrid' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true,
        NOW()
    ),
    (
        'Barcelona Tennis Academy',
        (SELECT id FROM locations WHERE city = 'Barcelona' LIMIT 1),
        (SELECT id FROM users WHERE email = 'admin@arenic.com' LIMIT 1),
        true,
        NOW()
    );