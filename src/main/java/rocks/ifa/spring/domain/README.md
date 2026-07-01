# 領域層 - 架構與設計原則

本文件闡述了此專案 `domain` 層的核心架構原則與設計慣例。所有開發者（包括我們的 AI 代理）都必須遵守這些準則，以維護一個高品質、可擴展且易於維護的程式碼庫。

---

## 1. 核心哲學：領域驅動設計 (Domain-Driven Design, DDD)

`domain` 層是我們應用程式的心臟。它包含了所有的核心業務邏輯、規則和模型。

- **業務邏輯的唯一歸宿**: 所有的業務規則都必須在領域層的實體 (Entities)、值物件 (Value Objects)、聚合 (Aggregates) 和領域服務 (Domain Services) 中實現。資料庫僅用於持久化，它應該是「貧血的 (Anemic)」，不應包含任何業務邏輯。
- **通用語言 (Ubiquitous Language)**: 所有的類別名稱、方法和變數，都必須與業務專家使用的語言保持一致。

---

## 2. 目錄結構約定

`domain` 套件是按 **聚合 (Aggregates)** 及其組成部分來組織的。

- **`client` (聚合根)**: 此套件包含 `ClientService` 和 `ClientController`，它們對 `Client` 這個聚合根進行整體操作，聚合其下子領域的資料。
- **`clientProfile`, `clientCareer`, `clientLaborPension` 等 (子領域 / 實體)**: 這些套件各自代表 `Client` 聚合根的一個獨立部分。它們應該包含：
    - `...Entity.java`: 代表資料結構的 JPA 實體。
    - `...Repository.java`: 用於資料存取的 Spring Data JPA 倉儲。
    - `...Service.java` & `...ServiceImpl.java`: 負責該特定子領域業務邏輯的服務。
    - `...Controller.java`: 暴露該子領域資源的 API 控制器。
    - `contracts/`: 用於存放所有資料傳輸物件 (DTOs) 的子套件。

---

## 3. DTO 命名與設計：CQRS 原則

我們嚴格分離用於讀取 (`Query`) 的模型和用於寫入 (`Command`) 的模型。

- **請求 DTOs (`...Req.java`)**:
    - **目的**: 承載**從客戶端到伺服器**的資料，用於**寫入操作** (例如 `create`, `update`)。
    - **命名**: 必須以 `Req` 結尾 (例如 `UpdateLaborPensionReq`)。
    - **內容**: 應該只包含用戶被**允許編輯**的欄位。它**絕不能**包含由伺服器計算的欄位。

- **回應 DTOs (`...Res.java`)**:
    - **目的**: 承載**從伺服器到客戶端**的資料，用於**讀取操作**。
    - **命名**: 必須以 `Res` 結尾 (例如 `LaborPensionRes`)。
    - **內容**: 應該包含客戶端需要顯示的所有欄位，包括用戶提供的欄位和**由伺服器計算的欄位** (例如 `predictedLumpSum`)。

**這種分離是不可協商的。** 它可以防止客戶端覆寫伺服器端的邏輯，並允許讀取和寫入模型可以獨立演進。

---

## 4. API 設計：巢狀資源 (Nested Resources)

為了清晰地表達 `Client` 聚合根與其子領域之間的關係，我們使用巢狀的資源 URL 結構。

- **模式**: `/api/v1/clients/{clientId}/{sub-resource}`
- **好的範例**:
    - `GET /api/v1/clients/{clientId}/careers`
    - `PUT /api/v1/clients/{clientId}/labor-pension`
- **壞的範例 (應避免)**:
    - `GET /api/v1/client-careers/{clientId}`

這個模式讓 API 能夠自我解釋，並使其結構與我們的 DDD 模型保持一致。

---

## 5. 資料庫與持久化：「貧血資料儲存」原則

- **🔥 禁止使用預存程序實現業務邏輯**: 在預存程序中實現業務規則是公然違反 DDD 的行為。它將我們的應用程式與資料庫耦合，摧毀了可測試性，並使業務邏輯四處分散。只有在極少數的、離線的、大規模資料維護任務中，才能在團隊批准後考慮使用預存程序。
- **Flyway 管結構，JPA 管資料**:
    - **Flyway**: 是**資料庫結構變更** (`CREATE TABLE`, `ALTER TABLE`) 的唯一權威來源。所有的結構修改都必須透過一個新的、帶有版本的遷移腳本來完成。**絕不允許修改一個已經被執行過的遷移腳本。**
    - **JPA/Hibernate**: 是所有**資料操作** (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) 的工具。我們利用 Spring Data JPA 的倉儲來實現這一點。

---

## 6. 安全性：「在服務層進行授權」

- **所有權檢查是強制性的**: 服務層中，任何存取或修改特定客戶資料的方法，都**必須**執行所有權檢查。
- **實現方式**: 檢查必須驗證 `requesterUid` (來自安全上下文) 是否與資料關聯的 `agent_firebase_uid` 或 `client_firebase_uid` 相匹配。
- **檢查位置**: 這個檢查應該是任何操作特定資源 ID 的公開服務方法中的第一步 (例如 `getCareer(clientId, requesterUid)`, `updateTax(clientId, req, requesterUid)`)。
