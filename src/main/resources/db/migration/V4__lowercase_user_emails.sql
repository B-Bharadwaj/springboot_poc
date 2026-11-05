-- Emails are now stored lower-case; convert existing rows to match.
UPDATE users SET email = LOWER(email);
