--
-- Table structure for table `client_credit_cards`
--
CREATE TABLE client_credit_cards (
    -- The credit card's own unique primary key
    id UUID PRIMARY KEY,

    -- Foreign key to the client_profiles table
    client_id UUID NOT NULL,

    -- Audit fields
    agent_firebase_uid VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Fields from ClientCreditCardEntity
    name VARCHAR(255) NOT NULL,
    deduction_account VARCHAR(255),
    usage_type VARCHAR(255),
    storage_location VARCHAR(255),
    average_monthly_expense NUMERIC(19, 4),

    -- Foreign key constraint to the client_profiles table
    CONSTRAINT fk_credit_card_client FOREIGN KEY (client_id) REFERENCES client_profiles(id) ON DELETE CASCADE
);

-- Add an index on the foreign key column for performance
CREATE INDEX idx_credit_card_client_id ON client_credit_cards(client_id);

COMMENT ON TABLE client_credit_cards IS '客戶的信用卡資料，用於支出管理與現金流追蹤';
COMMENT ON COLUMN client_credit_cards.id IS '信用卡紀錄的唯一主鍵';
COMMENT ON COLUMN client_credit_cards.client_id IS '指向 client_profiles.id 的外鍵';
COMMENT ON COLUMN client_credit_cards.agent_firebase_uid IS '建立或最後修改此紀錄的顧問的 Firebase UID';
COMMENT ON COLUMN client_credit_cards.name IS '卡片名稱 (e.g. 玉山 U Bear)';
COMMENT ON COLUMN client_credit_cards.deduction_account IS '扣款帳戶 (e.g. 台新 Richart)';
COMMENT ON COLUMN client_credit_cards.usage_type IS '用途分類 (e.g. online, daily)';
COMMENT ON COLUMN client_credit_cards.storage_location IS '存放位置: 錢包(wallet) / 數位(digital) / 抽屜(drawer)';
COMMENT ON COLUMN client_credit_cards.average_monthly_expense IS '平均月刷卡金額';
