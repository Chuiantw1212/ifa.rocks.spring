-- Create the client_career table to store career and income details for a client
CREATE TABLE client_careers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- Foreign key to the client_profiles table, ensuring a one-to-one relationship
    client_id UUID NOT NULL UNIQUE,

    -- Income Details
    base_salary NUMERIC(19, 4),
    other_allowance NUMERIC(19, 4),
    annual_bonus NUMERIC(19, 4),

    -- Deductions
    labor_insurance NUMERIC(19, 4),
    health_insurance NUMERIC(19, 4),
    other_deduction NUMERIC(19, 4),

    -- Pension Details
    pension_personal_rate NUMERIC(5, 2), -- e.g., 6.00 for 6%

    -- Stock Options
    stock_deduction NUMERIC(19, 4),
    stock_company_match NUMERIC(19, 4),

    -- Other Details
    dependents INTEGER,

    -- Timestamps
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    -- Constraint to link to the client_profiles table
    CONSTRAINT fk_client_profile
        FOREIGN KEY(client_id)
        REFERENCES client_profiles(id)
        ON DELETE CASCADE
);

-- Optional: Create an index on the foreign key for better query performance
CREATE INDEX IF NOT EXISTS idx_client_careers_client_id ON client_careers(client_id);
