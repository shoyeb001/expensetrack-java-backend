CREATE TABLE budgets(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    category_id UUID,
    name VARCHAR(100) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    period VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_budget_user FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_budget_category FOREIGN KEY (category_id) REFERENCES categories(id),

    CONSTRAINT chk_budget_amount CHECK (amount>0),

    CONSTRAINT chk_budget_dates CHECK (end_date>=start_date),

    CONSTRAINT chk_budget_period CHECK (period IN ('WEEKLY', 'MONTHLY', 'CUSTOM'))
);

CREATE INDEX idx_budget_user ON budgets(user_id);
CREATE INDEX idx_budget_category ON budgets(category_id);