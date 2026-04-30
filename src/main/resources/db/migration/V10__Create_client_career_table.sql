-- V10: Create the client_career table to store career and income details for a client.
-- This table has a one-to-one relationship with client_profiles.
CREATE TABLE client_careers (
    -- This ID is both the Primary Key and the Foreign Key to client_profiles.id
    id UUID PRIMARY KEY,

    -- Inherited from ClientBaseEntity, tracks which agent created/last modified this record.
    agent_firebase_uid VARCHAR(255) NOT NULL,

    -- === User-Editable Fields ===

    -- Income Details
    base_salary NUMERIC(19, 2),
    other_allowance NUMERIC(19, 2),
    annual_bonus NUMERIC(19, 2),

    -- Deductions
    labor_insurance NUMERIC(19, 2),
    health_insurance NUMERIC(19, 2),
    other_deduction NUMERIC(19, 2),

    -- Pension Details
    pension_personal_rate NUMERIC(5, 2), -- e.g., 6.00 for 6%

    -- Stock Options
    stock_deduction NUMERIC(19, 2),
    stock_company_match NUMERIC(19, 2),

    -- Other Details
    dependents INTEGER,

    -- === Backend-Calculated Fields ===

    pension_personal_amount NUMERIC(19, 2),
    pension_employer_amount NUMERIC(19, 2),
    pension_total_amount NUMERIC(19, 2),
    monthly_net_income NUMERIC(19, 2),
    annual_total_income NUMERIC(19, 2),

    -- === Timestamps ===

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- === Constraints ===

    CONSTRAINT fk_client_career_profile
        FOREIGN KEY(id)
        REFERENCES client_profiles(id)
        ON DELETE CASCADE
);

-- Create an index on the agent_firebase_uid for faster lookups
CREATE INDEX IF NOT EXISTS idx_client_careers_agent_firebase_uid ON client_careers(agent_firebase_uid);
