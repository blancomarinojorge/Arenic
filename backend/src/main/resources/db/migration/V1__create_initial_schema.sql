-- 1. Core Entities
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname1 VARCHAR(255) NOT NULL,
    surname2 VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
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

CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    address_line_1 VARCHAR(255) NOT NULL,
    address_line_2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state_province VARCHAR(100),
    zip_code VARCHAR(20) NOT NULL,
    country_code CHAR(2) NOT NULL DEFAULT 'ES',
    latitude DECIMAL(9, 6),
    longitude DECIMAL(9, 6),
    formatted_address TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Multi-Tenancy & Roles (N:M)
CREATE TABLE club_memberships (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    club_id UUID REFERENCES clubs(id) ON DELETE CASCADE,
    assigned_role user_role NOT NULL DEFAULT 'MEMBER',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, club_id)
);

-- 3. Infrastructure (Courts & Historized Pricing)
CREATE TABLE courts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    club_id UUID REFERENCES clubs(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    court_type varchar(50) REFERENCES court_types(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE court_types ( -- e.g., 'Clay', 'Grass', 'Hard'
    slug varchar(255) PRIMARY KEY,
    display_name varchar(100),
    short_display_name varchar(50),
    description varchar(250)
);

CREATE TABLE price_configurations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    week_day smallint NOT NULL,
    guest_price DECIMAL(12, 2) NOT NULL,
    member_price DECIMAL(12, 2) NOT NULL,
    -- Historization: valid_until is NULL for the current active price
    valid_from TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valid_until TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    CHECK (valid_until IS NULL OR valid_until > valid_from)
);

CREATE TABLE schedule_configurations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    slot_duration smallint NOT NULL,
    week_day smallint NOT NULL,
    opening_time TIMESTAMP WITH TIME ZONE NOT NULL,
    closing_time TIMESTAMP WITH TIME ZONE NOT NULL,
    -- Historization: valid_until is NULL for the current active schedule
    valid_from TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valid_until TIMESTAMP WITH TIME ZONE,
    CHECK (valid_until IS NULL OR valid_until > valid_from)
);

-- 4. Bookings
CREATE TABLE bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    court_id UUID REFERENCES courts(id) ON DELETE CASCADE,
    initiator_id UUID REFERENCES users(id), -- The "Creator" of the booking
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    total_price DECIMAL(12, 2) NOT NULL,
    booking_status_slug varchar(255) NOT NULL REFERENCES booking_statuses(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    booking_type varchar(50) NOT NULL REFERENCES booking_types(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    -- Snapshot of price at the moment of booking (Financial Integrity)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE,
    CHECK (end_time > start_time)
);

CREATE TABLE booking_participants(
    booking_id UUID REFERENCES bookings(id),
    user_id UUID REFERENCES users(id),
    is_owner boolean NOT NULL DEFAULT false,
    amount_to_pay DECIMAL(12, 2) NOT NULL,
    participation_status_slug varchar(255) NOT NULL REFERENCES booking_participation_statuses(slug) ON DELETE RESTRICT ON UPDATE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(booking_id, user_id)
);

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

-- 5. Payments
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID REFERENCES bookings(id),
    beneficiary_user_id UUID REFERENCES users(id),
    payer_user_id UUID REFERENCES users(id),
    amount DECIMAL(12, 2) NOT NULL,
    user_payment_method UUID NOT NULL REFERENCES user_payment_methods(id),
    payment_status_slug varchar(255) NOT NULL REFERENCES payment_statuses(slug),
    payment_trigger_slug varchar(255) REFERENCES payment_triggers(slug),
    -- Financial Safety: Prevent double-charging
    idempotency_key VARCHAR(255) UNIQUE,
    intent_id VARCHAR(255) UNIQUE,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- Column Comments
COMMENT ON COLUMN payments.payer_user_id IS 'The user who is providing the funds.';
COMMENT ON COLUMN payments.beneficiary_user_id IS 'The user receiving the funds, as users can pay for other users.';
COMMENT ON COLUMN payments.user_payment_method IS 'Reference to the saved payment instrument used for this transaction.';
COMMENT ON COLUMN payments.idempotency_key IS 'Internal unique key to prevent duplicate charges. Should be checked by the backend before calling the external gateway.';
COMMENT ON COLUMN payments.intent_id IS 'External reference ID from the payment provider (e.g., Stripe PaymentIntent ID) used for reconciliation and webhooks.';
COMMENT ON COLUMN payments.scheduled_at IS 'The target time the system is programmed to execute this charge (e.g., Booking End + 1h).';
COMMENT ON COLUMN payments.processed_at IS 'The actual time the payment gateway (Stripe/PayPal) confirmed the transaction success.';

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

CREATE TABLE user_payment_methods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token varchar(100),
    is_default boolean,
    card_last_four_digits varchar(4),
    payment_provider_id UUID NOT NULL REFERENCES payment_provider(id)
);

CREATE TABLE payment_provider(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255),
    is_active boolean
);

-- 6. Indexes for Performance (High Traffic Bookings)
CREATE INDEX idx_bookings_time ON bookings (start_time, end_time);
CREATE INDEX idx_payments_booking ON payments (booking_id);
CREATE INDEX idx_price_history ON price_configurations (court_id, valid_from, valid_until);
CREATE INDEX idx_locations_city ON locations(city);
CREATE INDEX idx_locations_zip ON locations(zip_code);