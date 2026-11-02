CREATE TABLE categories(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    category_type VARCHAR(20) NOT NULL,
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    icon VARCHAR(100),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_categories_type CHECK (category_type IN ('INCOME', 'EXPENSE')),

    CONSTRAINT chk_categories_owner CHECK(
        (is_system = TRUE AND user_id IS NULL) OR (is_system=FALSE AND user_id IS NOT NULL)
    )

);

--system category name must be unique

CREATE UNIQUE INDEX uq_system_category_name_type ON categories (LOWER(name), category_type) WHERE is_system = TRUE;

-- Each user can have their own custom category

CREATE UNIQUE INDEX uq_custom_category_name_type ON categories (user_id, LOWER(name), category_type) WHERE is_system = FALSE;

CREATE INDEX idx_categories_user_type ON categories (user_id, category_type);

ALTER TABLE transactions ADD COLUMN category_id UUID;

ALTER TABLE transactions ADD CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories(id);

CREATE INDEX idx_transaction_category_id ON transactions(category_id);