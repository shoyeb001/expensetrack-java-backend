CREATE TABLE users(
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT uq_users_email UNIQUE(email)
);

ALTER TABLE transactions
   ADD COLUMN user_id UUID;

ALTER TABLE transactions
   ADD CONSTRAINT fk_transactions_user
   FOREIGN KEY (user_id)
   REFERENCES users(id);

CREATE INDEX idx_transactions_user_date ON transactions(user_id, transaction_date DESC);
