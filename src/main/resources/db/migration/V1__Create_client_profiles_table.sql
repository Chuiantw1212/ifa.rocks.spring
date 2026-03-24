-- Enable the uuid-ossp extension to generate UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the client_profiles table with a UUID primary key
CREATE TABLE client_profiles (
    -- The primary key is now a UUID, which we will generate in the application
    id UUID PRIMARY KEY,

    -- Foreign key to the agent (Firebase User) who owns this client profile
    agent_firebase_uid VARCHAR(255) NOT NULL,

    -- Core client information
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    lineId VARCHAR(255) NOT NULL,

    -- Business rule: The email must be unique across all clients in the system.
    email VARCHAR(255) NOT NULL UNIQUE,

    -- Optional demographic and profile information
    birth_date DATE,
    gender VARCHAR(255),
    current_age INTEGER,
    life_expectancy INTEGER,
    marriage_year INTEGER,
    career_insurance_type VARCHAR(255),
    biography TEXT,

    -- Timestamps for auditing
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);
