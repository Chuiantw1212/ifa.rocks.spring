CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE client_profiles (
    id UUID PRIMARY KEY,
    agent_firebase_uid VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    birth_date DATE,
    gender VARCHAR(255),
    current_age INTEGER,
    life_expectancy INTEGER,
    marriage_year INTEGER,
    career_insurance_type VARCHAR(255),
    biography TEXT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    
    UNIQUE (agent_firebase_uid, email)
);
