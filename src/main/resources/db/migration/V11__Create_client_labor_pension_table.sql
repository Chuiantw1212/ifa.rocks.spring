-- V11: Create the client_labor_pensions table
CREATE TABLE client_labor_pensions (
    -- This ID is both the Primary Key and the Foreign Key to client_profiles.id
    id UUID PRIMARY KEY,

    -- Inherited from ClientBaseEntity
    agent_firebase_uid VARCHAR(255) NOT NULL,

    -- Labor Pension Details
    expected_retirement_age INTEGER,
    remaining_life_at_retirement INTEGER,
    retirement_roi NUMERIC(5, 2),
    employer_contribution NUMERIC(19, 2),
    employer_earnings NUMERIC(19, 2),
    personal_contribution NUMERIC(19, 2),
    personal_earnings NUMERIC(19, 2),
    current_work_seniority INTEGER,
    predicted_lump_sum NUMERIC(19, 2),
    predicted_net_lump_sum NUMERIC(19, 2),

    -- Timestamps
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraint to link to the client_profiles table
    CONSTRAINT fk_client_labor_pension_profile
        FOREIGN KEY(id)
        REFERENCES client_profiles(id)
        ON DELETE CASCADE
);
