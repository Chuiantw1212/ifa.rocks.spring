--
-- Table structure for table `client_credit_cards`
--
CREATE TABLE client_credit_cards (
    -- Inherited from ClientBaseEntity, overridden column name
    client_id UUID PRIMARY KEY,

    -- Inherited from ClientBaseEntity
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
    CONSTRAINT fk_client_credit_cards_client_id FOREIGN KEY (client_id) REFERENCES client_profiles(id) ON DELETE CASCADE
);

-- Optional: Add indexes for frequently queried columns
CREATE INDEX idx_client_credit_cards_agent_uid ON client_credit_cards(agent_firebase_uid);

COMMENT ON TABLE client_credit_cards IS '客戶的信用卡資料，用於支出管理與現金流追蹤';
COMMENT ON COLUMN client_credit_cards.client_id IS '主鍵，同時也是指向 client_profiles.id 的外鍵';
COMMENT ON COLUMN client_credit_cards.agent_firebase_uid IS '建立或最後修改此紀錄的顧問的 Firebase UID';
COMMENT ON COLUMN client_credit_cards.name IS '卡片名稱 (e.g. 玉山 U Bear)';
COMMENT ON COLUMN client_credit_cards.deduction_account IS '扣款帳戶 (e.g. 台新 Richart)';
COMMENT ON COLUMN client_credit_cards.usage_type IS '用途分類 (e.g. online, daily)';
COMMENT ON COLUMN client_credit_cards.storage_location IS '存放位置: 錢包(wallet) / 數位(digital) / 抽屜(drawer)';
COMMENT ON COLUMN client_credit_cards.average_monthly_expense IS '平均月刷卡金額';
