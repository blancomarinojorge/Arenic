-- ============================================================
-- LOOKUP TABLES
-- ============================================================

INSERT INTO club_membership_roles (slug, display_name, short_display_name, description) VALUES
('owner',   'Owner',   'Owner',   'Club owner with full administrative access'),
('manager', 'Manager', 'Mgr',     'Club manager with operational access'),
('staff',   'Staff',   'Staff',   'Club staff with limited access'),
('player',  'Player',  'Player',  'Regular club member / player');

INSERT INTO court_types (slug, display_name, short_display_name, description) VALUES
('PADEL',        'Pádel',         'Pádel',   'Outdoor or indoor padel court'),
('TENNIS_HARD',  'Tenis (Dura)',   'Tenis',   'Hard surface tennis court'),
('TENNIS_GRASS', 'Tenis (Hierba)', 'Hierba',  'Grass tennis court'),
('PICKLEBALL',   'Pickleball',     'PKB',     'Pickleball court');

INSERT INTO game_modes (slug, display_name, short_display_name, description, number_of_players) VALUES
('SINGLES', 'Individual', '1v1', 'Singles match — 2 players', 2),
('DOUBLES', 'Dobles',     '2v2', 'Doubles match — 4 players', 4);

INSERT INTO booking_statuses (slug, display_name, short_display_name, description) VALUES
('pending',   'Pendiente',  'Pend.',   'Booking created but not yet confirmed'),
('confirmed', 'Confirmada', 'Conf.',   'Booking confirmed and court reserved'),
('cancelled', 'Cancelada',  'Canc.',   'Booking cancelled by user or club'),
('completed', 'Completada', 'Comp.',   'Match has taken place');

INSERT INTO booking_types (slug, display_name, short_display_name, description) VALUES
('private', 'Privada', 'Priv.', 'Private booking — only invited players'),
('open',    'Abierta', 'Open',  'Open booking — any player can join');

INSERT INTO booking_participation_statuses (slug, display_name, short_display_name, description) VALUES
('invited',  'Invitado',  'Inv.',  'Player has been invited'),
('accepted', 'Aceptado',  'Acep.', 'Player accepted the invitation'),
('declined', 'Rechazado', 'Dec.',  'Player declined the invitation'),
('paid',     'Pagado',    'Pag.',  'Player has paid their share');

INSERT INTO payment_statuses (slug, display_name, short_display_name, description) VALUES
('pending',   'Pendiente', 'Pend.', 'Payment not yet processed'),
('succeeded', 'Completado','Comp.', 'Payment successful'),
('failed',    'Fallido',   'Fail.', 'Payment failed');

INSERT INTO payment_triggers (slug, display_name, short_display_name, description) VALUES
('booking_creation', 'Al crear la reserva', 'Creación', 'Charged when booking is created'),
('post_match',       'Tras el partido',      'Post',     'Charged after match completes');

INSERT INTO payment_providers (slug, display_name, short_display_name, description) VALUES
('stripe', 'Stripe', 'Stripe', 'Stripe payment gateway');

-- ============================================================
-- DEMO CLUBS
-- NOTE: clubs.creator_id must reference a real user.
-- After creating your account (via /auth/login or Google),
-- run V3__seed_clubs.sql.example replacing :YOUR_USER_ID
-- with your actual user UUID from the users table.
-- ============================================================
