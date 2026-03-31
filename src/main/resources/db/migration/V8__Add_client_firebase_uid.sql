-- Add a column to store the client's own Firebase UID, which can be nullable
ALTER TABLE client_profiles ADD COLUMN IF NOT EXISTS client_firebase_uid VARCHAR(255);
