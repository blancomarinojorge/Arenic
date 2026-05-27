-- 1. Core Entities
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname1 VARCHAR(255) NOT NULL,
    surname2 VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    address_line_1 VARCHAR(255) NOT NULL,
    address_line_2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state_province VARCHAR(100),
    zip_code VARCHAR(20) NOT NULL,
    country_code VARCHAR(2) NOT NULL DEFAULT 'ES',
    latitude DECIMAL(9, 6),
    longitude DECIMAL(9, 6),
    formatted_address TEXT,
    timezone VARCHAR(50) NOT NULL DEFAULT 'Europe/Madrid'
);

CREATE TABLE clubs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    location_id UUID REFERENCES locations(id),
    -- Ownership Integrity: Cannot delete user if they own a club
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    is_active boolean NOT NULL default true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE club_schedules (
    club_id UUID NOT NULL REFERENCES clubs(id) ON DELETE CASCADE,
    week_day_ordinal SMALLINT NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,

    PRIMARY KEY (club_id, week_day_ordinal),

    -- Keep our 1-7 data safety guard
    CONSTRAINT chk_valid_schedule_day CHECK (week_day_ordinal BETWEEN 1 AND 7)
);

-- 2. Multi-Tenancy & Roles (N:M)
CREATE TABLE club_membership_roles (
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE club_memberships (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    club_id UUID REFERENCES clubs(id) ON DELETE CASCADE,
    club_membership_role_slug varchar(255) REFERENCES club_membership_roles(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, club_id, club_membership_role_slug)
);

-- 3. Infrastructure (Courts & Historized Pricing)
CREATE TABLE court_types ( -- e.g., 'Clay', 'Grass', 'Hard'
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE courts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    club_id UUID REFERENCES clubs(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    court_type varchar(255) REFERENCES court_types(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE game_modes(
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250),
    number_of_players smallint NOT NULL
);

CREATE TABLE court_game_modes(
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    game_mode_slug varchar(255) REFERENCES game_modes(slug) ON DELETE RESTRICT ON UPDATE CASCADE,

    PRIMARY KEY (court_id, game_mode_slug)
);

CREATE TABLE price_rules(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    club_id UUID REFERENCES clubs(id) ON DELETE CASCADE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE price_rule_courts(
    price_rule_id UUID REFERENCES price_rules(id) ON DELETE CASCADE,
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,

    PRIMARY KEY(price_rule_id, court_id)
);

CREATE TABLE price_rule_day(
    price_rule_id UUID REFERENCES price_rules(id) ON DELETE CASCADE,
    week_day_ordinal SMALLINT NOT NULL,

    PRIMARY KEY(price_rule_id, week_day_ordinal),

    CONSTRAINT chk_valid_price_rule_day CHECK (week_day_ordinal BETWEEN 1 AND 7)
);

CREATE TABLE price_rule_intervals(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    price_rule_id UUID REFERENCES price_rules(id) ON DELETE CASCADE,
    interval_minutes SMALLINT NOT NULL,
    total_price DECIMAL(12, 2) NOT NULL CHECK (total_price >= 0),
    currency CHAR(3) NOT NULL DEFAULT 'EUR' CHECK (currency ~ '^[A-Z]{3}$'),
    member_discount_percent SMALLINT NOT NULL,
    game_mode varchar(255) REFERENCES game_modes(slug) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 4. Bookings
CREATE TABLE booking_statuses (
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE booking_types (
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE booking_participation_statuses(
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    court_id UUID REFERENCES courts(id) ON DELETE RESTRICT,
    initiator_id UUID REFERENCES users(id), -- The "Creator" of the booking
    game_mode varchar(255) REFERENCES game_modes(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    base_total_price DECIMAL(12, 2) NOT NULL CHECK (base_total_price >= 0),
    final_total_price DECIMAL(12, 2) NOT NULL CHECK (final_total_price >= 0),
    currency CHAR(3) NOT NULL DEFAULT 'EUR' CHECK (currency ~ '^[A-Z]{3}$'),
    booking_status varchar(255) NOT NULL REFERENCES booking_statuses(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    booking_type varchar(255) NOT NULL REFERENCES booking_types(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    CHECK (end_time > start_time)
);

CREATE TABLE booking_participants(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID REFERENCES bookings(id) ON DELETE CASCADE,
    participant_user_id UUID REFERENCES users(id) ON DELETE RESTRICT,
    payer_user_id UUID REFERENCES users(id) ON DELETE RESTRICT,
    is_owner boolean NOT NULL DEFAULT false,
    amount_to_pay DECIMAL(12, 2) NOT NULL CHECK (amount_to_pay >= 0),
    currency CHAR(3) NOT NULL DEFAULT 'EUR' CHECK (currency ~ '^[A-Z]{3}$'),
    participation_status varchar(255) NOT NULL REFERENCES booking_participation_statuses(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Payments

CREATE TABLE payment_statuses (
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE payment_triggers (
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE payment_providers(
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE user_payment_methods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token varchar(100),
    is_default boolean,
    card_last_four_digits varchar(4),
    payment_provider varchar(255) NOT NULL REFERENCES payment_providers(slug) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID REFERENCES bookings(id) ON DELETE RESTRICT,
    user_id UUID REFERENCES users(id) ON DELETE RESTRICT,
    amount DECIMAL(12, 2) NOT NULL CHECK (amount > 0),
    currency CHAR(3) NOT NULL DEFAULT 'EUR' CHECK (currency ~ '^[A-Z]{3}$'),
    user_payment_method UUID NOT NULL REFERENCES user_payment_methods(id),
    payment_status varchar(255) NOT NULL REFERENCES payment_statuses(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    payment_trigger varchar(255) REFERENCES payment_triggers(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    -- Financial Safety: Prevent double-charging
    idempotency_key VARCHAR(255) UNIQUE,
    intent_id VARCHAR(255) UNIQUE,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Column Comments
COMMENT ON COLUMN payments.user_id IS 'The user who is providing the funds.';
COMMENT ON COLUMN payments.user_payment_method IS 'Reference to the saved payment instrument used for this transaction.';
COMMENT ON COLUMN payments.idempotency_key IS 'Internal unique key to prevent duplicate charges. Should be checked by the backend before calling the external gateway.';
COMMENT ON COLUMN payments.intent_id IS 'External reference ID from the payment provider (e.g., Stripe PaymentIntent ID) used for reconciliation and webhooks.';
COMMENT ON COLUMN payments.scheduled_at IS 'The target time the system is programmed to execute this charge (e.g., Booking End + 1h).';
COMMENT ON COLUMN payments.processed_at IS 'The actual time the payment gateway (Stripe/PayPal) confirmed the transaction success.';

-- 6. Indexes for Performance (High Traffic Bookings)
CREATE INDEX idx_bookings_court_availability ON bookings (court_id, start_time, end_time);
CREATE INDEX idx_booking_participants ON booking_participants (booking_id);
CREATE INDEX idx_payments_booking ON payments (booking_id);
CREATE INDEX idx_locations_city ON locations(city);
CREATE INDEX idx_locations_zip ON locations(zip_code);
CREATE INDEX idx_price_rules_club ON price_rules (club_id);


-- 7. Token refresh
CREATE TABLE user_refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL
);