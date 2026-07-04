-- This migration modifies the 'agents' table to allow the 'email' column to be NULL.
-- This is necessary because not all authentication providers (e.g., LINE Login without email permission)
-- supply an email address, and our system must be able to create an agent record even in that case.
ALTER TABLE agents ALTER COLUMN email DROP NOT NULL;
