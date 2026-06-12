-- Run this manually AFTER creating your account.
-- Replace '00000000-0000-0000-0000-000000000000' with your real user UUID:
--   SELECT id FROM users WHERE email = 'your@email.com';

DO $$
DECLARE
  v_creator UUID := '00000000-0000-0000-0000-000000000000'; -- <-- REPLACE THIS
BEGIN

INSERT INTO locations (id, address_line_1, city, state_province, zip_code, country_code, latitude, longitude, formatted_address, timezone) VALUES
('10000000-0000-0000-0000-000000000001', 'Calle de Serrano 41',         'Madrid',    'Comunidad de Madrid', '28001', 'ES', 40.426660, -3.688640, 'Calle de Serrano 41, 28001 Madrid, España',             'Europe/Madrid'),
('10000000-0000-0000-0000-000000000002', 'Avenida de la Castellana 200','Madrid',    'Comunidad de Madrid', '28046', 'ES', 40.455980, -3.692430, 'Av. de la Castellana 200, 28046 Madrid, España',         'Europe/Madrid'),
('10000000-0000-0000-0000-000000000003', 'Passeig de Gràcia 55',        'Barcelona', 'Cataluña',            '08007', 'ES', 41.392010,  2.165440, 'Passeig de Gràcia 55, 08007 Barcelona, España',          'Europe/Madrid');

INSERT INTO clubs (id, name, location_id, creator_id, is_active) VALUES
('20000000-0000-0000-0000-000000000001', 'Arenic Serrano Club',      '10000000-0000-0000-0000-000000000001', v_creator, true),
('20000000-0000-0000-0000-000000000002', 'Arenic Castellana Sport',  '10000000-0000-0000-0000-000000000002', v_creator, true),
('20000000-0000-0000-0000-000000000003', 'Arenic Barcelona Gràcia',  '10000000-0000-0000-0000-000000000003', v_creator, true);

INSERT INTO club_memberships (user_id, club_id, club_membership_role_slug) VALUES
(v_creator, '20000000-0000-0000-0000-000000000001', 'owner'),
(v_creator, '20000000-0000-0000-0000-000000000002', 'owner'),
(v_creator, '20000000-0000-0000-0000-000000000003', 'owner');

INSERT INTO club_schedules (club_id, week_day_ordinal, opening_time, closing_time)
SELECT c.id, d.n, '08:00', '22:00'
FROM clubs c, (VALUES (1),(2),(3),(4),(5),(6),(7)) AS d(n)
WHERE c.id IN ('20000000-0000-0000-0000-000000000001','20000000-0000-0000-0000-000000000002','20000000-0000-0000-0000-000000000003');

INSERT INTO courts (id, club_id, name, court_type, is_active) VALUES
('30000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001', 'Pista 1 — Pádel',      'PADEL',       true),
('30000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000001', 'Pista 2 — Pádel',      'PADEL',       true),
('30000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000001', 'Pista 3 — Tenis',      'TENNIS_HARD', true),
('30000000-0000-0000-0000-000000000004', '20000000-0000-0000-0000-000000000001', 'Pista 4 — Tenis',      'TENNIS_HARD', true),
('30000000-0000-0000-0000-000000000005', '20000000-0000-0000-0000-000000000002', 'Pista 1 — Pádel',      'PADEL',       true),
('30000000-0000-0000-0000-000000000006', '20000000-0000-0000-0000-000000000002', 'Pista 2 — Pádel',      'PADEL',       true),
('30000000-0000-0000-0000-000000000007', '20000000-0000-0000-0000-000000000002', 'Pista 3 — Tenis',      'TENNIS_HARD', true),
('30000000-0000-0000-0000-000000000008', '20000000-0000-0000-0000-000000000003', 'Pista 1 — Pádel',      'PADEL',       true),
('30000000-0000-0000-0000-000000000009', '20000000-0000-0000-0000-000000000003', 'Pista 2 — Pádel',      'PADEL',       true),
('30000000-0000-0000-0000-000000000010', '20000000-0000-0000-0000-000000000003', 'Pista 3 — Pickleball', 'PICKLEBALL',  true);

INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT id, 'DOUBLES' FROM courts WHERE court_type = 'PADEL'       AND id LIKE '30000000%';
INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT id, 'SINGLES' FROM courts WHERE court_type = 'TENNIS_HARD' AND id LIKE '30000000%';
INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT id, 'DOUBLES' FROM courts WHERE court_type = 'TENNIS_HARD' AND id LIKE '30000000%';
INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT id, 'SINGLES' FROM courts WHERE court_type = 'PICKLEBALL'  AND id LIKE '30000000%';
INSERT INTO court_game_modes (court_id, game_mode_slug)
SELECT id, 'DOUBLES' FROM courts WHERE court_type = 'PICKLEBALL'  AND id LIKE '30000000%';

END $$;
