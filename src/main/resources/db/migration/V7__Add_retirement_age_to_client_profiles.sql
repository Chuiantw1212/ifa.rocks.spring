-- Add a customizable retirement age for each client profile
ALTER TABLE client_profiles ADD COLUMN IF NOT EXISTS retirement_age INTEGER;
