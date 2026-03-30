-- Enable the uuid-ossp extension to generate UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the client_profiles table with a UUID primary key
CREATE TABLE client_profiles (
    id UUID PRIMARY KEY,
    agent_firebase_uid VARCHAR(255) NOT NULL,
    client_firebase_uid VARCHAR(255), -- New field for client's own login
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    line_id VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,

    -- Demographic and profile information
    birth_date DATE,
    gender VARCHAR(255),
    current_age INTEGER,
    retirement_age INTEGER,
    life_expectancy INTEGER,
    life_expectancy_at_retirement INTEGER,
    marriage_year INTEGER,
    career_insurance_type VARCHAR(255),
    biography TEXT,

    -- Timestamps
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);
