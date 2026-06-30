-- V12: Create the client_labor_insurances table for old-age annuity
CREATE TABLE client_labor_insurances (
    -- This ID is both the Primary Key and the Foreign Key to client_profiles.id
    client_id UUID PRIMARY KEY,

    -- Inherited from ClientBaseEntity
    agent_firebase_uid VARCHAR(255) NOT NULL,

    -- Labor Insurance Old-Age Annuity Details
    expected_claim_age INTEGER,
    average_monthly_salary NUMERIC(19, 2),
    insurance_seniority INTEGER, -- in months
    predicted_remaining_life INTEGER, -- in years

    -- Backend-Calculated Field
    predicted_monthly_annuity NUMERIC(19, 2),

    -- Timestamps
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraint to link to the client_profiles table
    CONSTRAINT fk_client_labor_insurance_profile
        FOREIGN KEY(client_id)
        REFERENCES client_profiles(id)
        ON DELETE CASCADE
);
