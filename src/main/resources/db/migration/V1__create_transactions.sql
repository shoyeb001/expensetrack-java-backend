CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    transaction_date DATE NOT NULL,

    CONSTRAINT chk_transaction_amount CHECK (amount > 0),

    CONSTRAINT chk_transaction_type CHECK (transaction_type IN ('INCOME', 'EXPENSE'))
);