-- Create the client_career table to store career and income details for a client
CREATE TABLE client_careers (
    -- Inherited from ClientBaseEntity
    id UUID PRIMARY KEY,
    agent_firebase_uid VARCHAR(255) NOT NULL,

    -- Income Details (Editable by User)
    base_salary NUMERIC(19, 4),
    other_allowance NUMERIC(19, 4),
    annual_bonus NUMERIC(19, 4),

    -- Deductions (Editable by User)
    labor_insurance NUMERIC(19, 4),
    health_insurance NUMERIC(19, 4),
    other_deduction NUMERIC(19, 4),

    -- Pension Details (Editable by User)
    pension_personal_rate NUMERIC(5, 2),

    -- Stock Options (Editable by User)
    stock_deduction NUMERIC(19, 4),
    stock_company_match NUMERIC(19, 4),

    -- Other Details (Editable by User)
    dependents INTEGER,

    -- Calculated Fields (Managed by Backend)
    pension_personal_amount NUMERIC(19, 4),
    pension_employer_amount NUMERIC(19, 4),
    pension_total_amount NUMERIC(19, 4),
    monthly_net_income NUMERIC(19, 4),
    annual_total_income NUMERIC(19, 4),

    -- Timestamps
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    -- Constraint to link to the client_profiles table
    CONSTRAINT fk_client_career_profile
        FOREIGN KEY(id)
        REFERENCES client_profiles(id)
        ON DELETE CASCADE
);
