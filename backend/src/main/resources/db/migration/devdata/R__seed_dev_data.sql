-- ============================================================
-- R__seed_dev_data.sql  — Repeatable dev seed
-- Runs every time this file changes (Flyway checksum).
-- Safe to re-run: cleans previous dev data before inserting.
-- ============================================================

-- ============================================================
-- 0. CLEANUP  (FK-safe order)
-- ============================================================

-- Payments referencing dev bookings
DELETE FROM payments
WHERE booking_id IN (
    SELECT b.id FROM bookings b
    JOIN courts ct ON ct.id = b.court_id
    JOIN clubs cl  ON cl.id = ct.club_id
    JOIN users u   ON u.id  = cl.creator_id
    WHERE u.email = 'admin@arenic.com'
);

-- Booking participants (cascade from bookings, but explicit is cleaner)
DELETE FROM booking_participants
WHERE booking_id IN (
    SELECT b.id FROM bookings b
    JOIN courts ct ON ct.id = b.court_id
    JOIN clubs cl  ON cl.id = ct.club_id
    JOIN users u   ON u.id  = cl.creator_id
    WHERE u.email = 'admin@arenic.com'
);

-- Bookings tied to dev courts
DELETE FROM bookings
WHERE court_id IN (
    SELECT ct.id FROM courts ct
    JOIN clubs cl ON cl.id = ct.club_id
    JOIN users u  ON u.id  = cl.creator_id
    WHERE u.email = 'admin@arenic.com'
);

-- Dev clubs (cascades → courts, club_schedules, club_memberships)
DELETE FROM clubs
WHERE creator_id IN (SELECT id FROM users WHERE email = 'admin@arenic.com');

-- Dev locations (clubs deleted above, so FK is clear)
DELETE FROM locations WHERE address_line_1 IN (
    'Calle de Serrano, 12',
    'Calle de Alcalá, 45',
    'Calle Gran Vía, 78',
    'Calle de Velázquez, 33',
    'Avinguda de Diagonal, 405',
    'Carrer de Balmes, 200',
    'Carrer de Provença, 150',
    'Calle Larios, 5',
    'Calle San Fernando, 10',
    'Calle Colón, 22'
);

-- Dev users
DELETE FROM users WHERE email IN ('admin@arenic.com', 'player@arenic.com');

-- ============================================================
-- 1. USERS
-- Passwords (BCrypt 12 rounds):
--   admin@arenic.com  →  Admin1234
--   player@arenic.com →  Player1234
-- ============================================================
INSERT INTO users (email, password, name, surname1, surname2, created_at)
VALUES
    ('admin@arenic.com',  '$2a$12$UU48VkBs6YyRwSgkxqQHxO0VvaIMDJHBcBzo/V0.morPFCjD3NJSW', 'Carlos',  'Alcaraz', 'Admin', NOW()),
    ('player@arenic.com', '$2a$12$V3NHpICo9d5Gx.abEln2wepkIUJiJivBWqN8oU.vqfBsjhOlKQlRu', 'Rafa',    'Nadal',   'User',  NOW());

-- ============================================================
-- 2. LOCATIONS
-- ============================================================
INSERT INTO locations (address_line_1, city, zip_code, country_code, timezone)
VALUES
    ('Calle de Serrano, 12',      'Madrid',    '28001', 'ES', 'Europe/Madrid'),
    ('Calle de Alcalá, 45',       'Madrid',    '28014', 'ES', 'Europe/Madrid'),
    ('Calle Gran Vía, 78',        'Madrid',    '28013', 'ES', 'Europe/Madrid'),
    ('Calle de Velázquez, 33',    'Madrid',    '28001', 'ES', 'Europe/Madrid'),
    ('Avinguda de Diagonal, 405', 'Barcelona', '08006', 'ES', 'Europe/Madrid'),
    ('Carrer de Balmes, 200',     'Barcelona', '08006', 'ES', 'Europe/Madrid'),
    ('Carrer de Provença, 150',   'Barcelona', '08008', 'ES', 'Europe/Madrid'),
    ('Calle Larios, 5',           'Málaga',    '29005', 'ES', 'Europe/Madrid'),
    ('Calle San Fernando, 10',    'Sevilla',   '41004', 'ES', 'Europe/Madrid'),
    ('Calle Colón, 22',           'Valencia',  '46004', 'ES', 'Europe/Madrid');

-- ============================================================
-- 3. CLUBS
-- ============================================================
INSERT INTO clubs (name, location_id, creator_id, is_active, created_at)
VALUES
    ('Real Madrid Padel Center',  (SELECT id FROM locations WHERE address_line_1 = 'Calle de Serrano, 12'      LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Madrid Sports Club',        (SELECT id FROM locations WHERE address_line_1 = 'Calle de Alcalá, 45'       LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Gran Vía Tennis Academy',   (SELECT id FROM locations WHERE address_line_1 = 'Calle Gran Vía, 78'        LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Velázquez Padel Club',      (SELECT id FROM locations WHERE address_line_1 = 'Calle de Velázquez, 33'    LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Barcelona Tennis Academy',  (SELECT id FROM locations WHERE address_line_1 = 'Avinguda de Diagonal, 405' LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Balmes Padel Center',       (SELECT id FROM locations WHERE address_line_1 = 'Carrer de Balmes, 200'     LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Eixample Sports Club',      (SELECT id FROM locations WHERE address_line_1 = 'Carrer de Provença, 150'   LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Málaga Padel & Tennis',     (SELECT id FROM locations WHERE address_line_1 = 'Calle Larios, 5'           LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Sevilla Racket Club',       (SELECT id FROM locations WHERE address_line_1 = 'Calle San Fernando, 10'    LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW()),
    ('Valencia Padel Arena',      (SELECT id FROM locations WHERE address_line_1 = 'Calle Colón, 22'           LIMIT 1), (SELECT id FROM users WHERE email = 'admin@arenic.com'), true, NOW());

-- ============================================================
-- 4. CLUB SCHEDULES  (Mon–Sun 08:00–22:00 for every club)
-- ============================================================
INSERT INTO club_schedules (club_id, week_day_ordinal, opening_time, closing_time)
SELECT c.id, d.n, '08:00'::time, '22:00'::time
FROM clubs c
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7)) AS d(n)
WHERE c.creator_id = (SELECT id FROM users WHERE email = 'admin@arenic.com');

-- ============================================================
-- 5. CLUB MEMBERSHIPS  (admin is OWNER of all dev clubs)
-- ============================================================
INSERT INTO club_memberships (user_id, club_id, club_membership_role_slug)
SELECT
    (SELECT id FROM users WHERE email = 'admin@arenic.com'),
    c.id,
    'owner'
FROM clubs c
WHERE c.creator_id = (SELECT id FROM users WHERE email = 'admin@arenic.com');

INSERT INTO club_memberships (user_id, club_id, club_membership_role_slug)
SELECT
    (SELECT id FROM users WHERE email = 'player@arenic.com'),
    c.id,
    'player'
FROM clubs c
WHERE c.creator_id = (SELECT id FROM users WHERE email = 'admin@arenic.com');

-- ============================================================
-- 6. COURTS
-- ============================================================

-- Real Madrid Padel Center — 4 pistas pádel + 2 tenis
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel Indoor','Pista 2 — Pádel Indoor','Pista 3 — Pádel Outdoor','Pista 4 — Pádel Outdoor']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Real Madrid Padel Center';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 5 — Tenis','Pista 6 — Tenis']),
       'TENNIS_HARD', true, NOW()
FROM clubs WHERE name = 'Real Madrid Padel Center';

-- Madrid Sports Club — 3 pádel + 2 tenis + 1 pickleball
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel','Pista 2 — Pádel','Pista 3 — Pádel']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Madrid Sports Club';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 4 — Tenis Hard','Pista 5 — Tenis Hard']),
       'TENNIS_HARD', true, NOW()
FROM clubs WHERE name = 'Madrid Sports Club';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, 'Pista 6 — Pickleball',
       'PICKLEBALL', true, NOW()
FROM clubs WHERE name = 'Madrid Sports Club';

-- Gran Vía Tennis Academy — 4 tenis hard + 2 tenis grass
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Tenis Hard','Pista 2 — Tenis Hard','Pista 3 — Tenis Hard','Pista 4 — Tenis Hard']),
       'TENNIS_HARD', true, NOW()
FROM clubs WHERE name = 'Gran Vía Tennis Academy';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 5 — Tenis Hierba','Pista 6 — Tenis Hierba']),
       'TENNIS_GRASS', true, NOW()
FROM clubs WHERE name = 'Gran Vía Tennis Academy';

-- Velázquez Padel Club — 4 pádel
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel','Pista 2 — Pádel','Pista 3 — Pádel','Pista 4 — Pádel']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Velázquez Padel Club';

-- Barcelona Tennis Academy — 3 tenis hard + 2 tenis grass + 1 pádel
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Tenis Hard','Pista 2 — Tenis Hard','Pista 3 — Tenis Hard']),
       'TENNIS_HARD', true, NOW()
FROM clubs WHERE name = 'Barcelona Tennis Academy';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 4 — Tenis Hierba','Pista 5 — Tenis Hierba']),
       'TENNIS_GRASS', true, NOW()
FROM clubs WHERE name = 'Barcelona Tennis Academy';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, 'Pista 6 — Pádel',
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Barcelona Tennis Academy';

-- Balmes Padel Center — 4 pádel + 1 pickleball
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel','Pista 2 — Pádel','Pista 3 — Pádel','Pista 4 — Pádel']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Balmes Padel Center';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, 'Pista 5 — Pickleball',
       'PICKLEBALL', true, NOW()
FROM clubs WHERE name = 'Balmes Padel Center';

-- Eixample Sports Club — 2 pádel + 2 tenis + 2 pickleball
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel','Pista 2 — Pádel']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Eixample Sports Club';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 3 — Tenis Hard','Pista 4 — Tenis Hard']),
       'TENNIS_HARD', true, NOW()
FROM clubs WHERE name = 'Eixample Sports Club';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 5 — Pickleball','Pista 6 — Pickleball']),
       'PICKLEBALL', true, NOW()
FROM clubs WHERE name = 'Eixample Sports Club';

-- Málaga Padel & Tennis — 3 pádel + 2 tenis
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel','Pista 2 — Pádel','Pista 3 — Pádel']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Málaga Padel & Tennis';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 4 — Tenis','Pista 5 — Tenis']),
       'TENNIS_HARD', true, NOW()
FROM clubs WHERE name = 'Málaga Padel & Tennis';

-- Sevilla Racket Club — 4 pádel
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel','Pista 2 — Pádel','Pista 3 — Pádel','Pista 4 — Pádel']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Sevilla Racket Club';

-- Valencia Padel Arena — 4 pádel + 1 tenis + 1 pickleball
INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, unnest(ARRAY['Pista 1 — Pádel','Pista 2 — Pádel','Pista 3 — Pádel','Pista 4 — Pádel']),
       'PADEL', true, NOW()
FROM clubs WHERE name = 'Valencia Padel Arena';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, 'Pista 5 — Tenis',
       'TENNIS_HARD', true, NOW()
FROM clubs WHERE name = 'Valencia Padel Arena';

INSERT INTO courts (club_id, name, court_type, is_active, created_at)
SELECT id, 'Pista 6 — Pickleball',
       'PICKLEBALL', true, NOW()
FROM clubs WHERE name = 'Valencia Padel Arena';

-- ============================================================
-- 7. COURT GAME MODES
-- Pádel       → DOUBLES only
-- Tenis       → SINGLES + DOUBLES
-- Pickleball  → SINGLES + DOUBLES
-- (Only for dev clubs to avoid touching unrelated courts)
-- ============================================================
INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT ct.id, 'DOUBLES'
FROM courts ct
JOIN clubs cl ON cl.id = ct.club_id
WHERE cl.creator_id = (SELECT id FROM users WHERE email = 'admin@arenic.com')
  AND ct.court_type = 'PADEL';

INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT ct.id, 'SINGLES'
FROM courts ct
JOIN clubs cl ON cl.id = ct.club_id
WHERE cl.creator_id = (SELECT id FROM users WHERE email = 'admin@arenic.com')
  AND ct.court_type IN ('TENNIS_HARD', 'TENNIS_GRASS', 'PICKLEBALL');

INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT ct.id, 'DOUBLES'
FROM courts ct
JOIN clubs cl ON cl.id = ct.club_id
WHERE cl.creator_id = (SELECT id FROM users WHERE email = 'admin@arenic.com')
  AND ct.court_type IN ('TENNIS_HARD', 'TENNIS_GRASS', 'PICKLEBALL');

-- ============================================================
-- 8. PRICE RULES
-- Three tiers per club (cascades cleanly when clubs are deleted):
--   Off-peak  Mon–Fri  08:00–17:00  (cheaper rates)
--   Peak Eve  Mon–Fri  17:00–22:00  (peak rates)
--   Weekend   Sat–Sun  08:00–22:00  (peak rates)
-- ============================================================
DO $$
DECLARE
    admin_id    UUID;
    club_rec    RECORD;
    offpeak_id  UUID;
    peak_id     UUID;
    weekend_id  UUID;
BEGIN
    SELECT id INTO admin_id FROM users WHERE email = 'admin@arenic.com';

    FOR club_rec IN
        SELECT id FROM clubs WHERE creator_id = admin_id
    LOOP

        -- ── Off-peak: Mon–Fri 08:00–17:00 ────────────────────
        INSERT INTO price_rules (club_id, start_time, end_time)
        VALUES (club_rec.id, '08:00', '17:00')
        RETURNING id INTO offpeak_id;

        INSERT INTO price_rule_day (price_rule_id, week_day_ordinal)
        SELECT offpeak_id, n FROM generate_series(1, 5) n;

        INSERT INTO price_rule_courts (price_rule_id, court_id)
        SELECT offpeak_id, id FROM courts WHERE club_id = club_rec.id;

        INSERT INTO price_rule_intervals
            (price_rule_id, interval_minutes, total_price, currency, member_discount_percent, game_mode)
        VALUES
            (offpeak_id, 60, 12.00, 'EUR', 10, 'DOUBLES'),
            (offpeak_id, 90, 17.00, 'EUR', 10, 'DOUBLES'),
            (offpeak_id, 60, 10.00, 'EUR', 10, 'SINGLES'),
            (offpeak_id, 90, 14.00, 'EUR', 10, 'SINGLES');

        -- ── Peak evening: Mon–Fri 17:00–22:00 ────────────────
        INSERT INTO price_rules (club_id, start_time, end_time)
        VALUES (club_rec.id, '17:00', '22:00')
        RETURNING id INTO peak_id;

        INSERT INTO price_rule_day (price_rule_id, week_day_ordinal)
        SELECT peak_id, n FROM generate_series(1, 5) n;

        INSERT INTO price_rule_courts (price_rule_id, court_id)
        SELECT peak_id, id FROM courts WHERE club_id = club_rec.id;

        INSERT INTO price_rule_intervals
            (price_rule_id, interval_minutes, total_price, currency, member_discount_percent, game_mode)
        VALUES
            (peak_id, 60, 18.00, 'EUR', 10, 'DOUBLES'),
            (peak_id, 90, 25.00, 'EUR', 10, 'DOUBLES'),
            (peak_id, 60, 15.00, 'EUR', 10, 'SINGLES'),
            (peak_id, 90, 21.00, 'EUR', 10, 'SINGLES');

        -- ── Weekend: Sat–Sun 08:00–22:00 ─────────────────────
        INSERT INTO price_rules (club_id, start_time, end_time)
        VALUES (club_rec.id, '08:00', '22:00')
        RETURNING id INTO weekend_id;

        INSERT INTO price_rule_day (price_rule_id, week_day_ordinal)
        VALUES (weekend_id, 6), (weekend_id, 7);

        INSERT INTO price_rule_courts (price_rule_id, court_id)
        SELECT weekend_id, id FROM courts WHERE club_id = club_rec.id;

        INSERT INTO price_rule_intervals
            (price_rule_id, interval_minutes, total_price, currency, member_discount_percent, game_mode)
        VALUES
            (weekend_id, 60, 20.00, 'EUR', 10, 'DOUBLES'),
            (weekend_id, 90, 28.00, 'EUR', 10, 'DOUBLES'),
            (weekend_id, 60, 17.00, 'EUR', 10, 'SINGLES'),
            (weekend_id, 90, 23.00, 'EUR', 10, 'SINGLES');

    END LOOP;
END $$;
