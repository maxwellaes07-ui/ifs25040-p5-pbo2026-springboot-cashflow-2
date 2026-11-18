CREATE DATABASE pbo2026_cashflow;
-- Jalankan perintah berikut setelah terkoneksi ke database pbo2026_cashflow.
CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  password_hash VARCHAR(64) NOT NULL,
  role VARCHAR(10) NOT NULL DEFAULT 'USER',
  photo_path VARCHAR(500)
);
CREATE TABLE IF NOT EXISTS transactions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  description VARCHAR(250) NOT NULL,
  amount BIGINT NOT NULL CHECK (amount > 0),
  type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME','EXPENSE')),
  proof_path VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
