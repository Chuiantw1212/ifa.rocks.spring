-- This table stores agent user information, linking different authentication methods
-- like Firebase UID and LINE User ID. It serves as the single source of truth for user identity.
CREATE TABLE agents (
    -- Primary key, a universally unique identifier. The application layer (Hibernate) will generate this.
    id UUID NOT NULL PRIMARY KEY,

    -- Unique identifier from Firebase Authentication. Can be null if the user only signed up with LINE.
    firebase_uid VARCHAR(255) UNIQUE,

    -- Unique identifier from LINE Login. Can be null if the user only signed up with email/password.
    line_user_id VARCHAR(255) UNIQUE,

    -- User's email address, used as a bridge for account linking. Can be null if the auth provider doesn't supply it.
    email VARCHAR(255) UNIQUE,

    -- User's display name.
    name VARCHAR(255),

    -- URL for the user's profile picture.
    picture_url TEXT
);

-- Add an explicit index on email for faster lookups, as it's a key part of the login/linking logic.
-- The UNIQUE constraint above often creates an index automatically, but this ensures it exists.
CREATE INDEX IF NOT EXISTS idx_agents_email ON agents(email);

-- Add indexes on the foreign UIDs for fast lookups during login.
CREATE INDEX IF NOT EXISTS idx_agents_firebase_uid ON agents(firebase_uid);
CREATE INDEX IF NOT EXISTS idx_agents_line_user_id ON agents(line_user_id);
