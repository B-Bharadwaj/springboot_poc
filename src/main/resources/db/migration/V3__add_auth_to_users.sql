-- Login data for users. Existing rows get role 'USER' and no password.
ALTER TABLE users ADD COLUMN password_hash VARCHAR(100);
ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

-- One account per email; also speeds up lookups by email.
CREATE UNIQUE INDEX ux_users_email ON users (email);
