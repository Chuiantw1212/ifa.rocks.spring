# Domain Layer

本套件是應用程式的**領域層 (Domain Layer)**，也是整個系統的核心。所有關鍵的業務邏輯、規則和狀態都必須封裝在此層。

## 設計原則 (Core Principles)

1.  **高內聚，低耦合 (High Cohesion, Low Coupling)**:
    -   所有與特定業務概念相關的邏輯都應該被組織在對應的聚合 (Aggregate) 內。
    -   領域層對外部世界一無所知，它不依賴任何 `application` 層或 `infrastructure` 層的具體實現。

2.  **豐富領域模型 (Rich Domain Model)**:
    -   禁止使用貧血模型（Anemic Domain Model）。實體 (Entity) 應該包含其對應的業務行為，而不僅僅是數據的載體。
    -   狀態的變更必須透過實體自身的方法來完成，並確保所有業務不變性 (Invariants) 在變更過程中得到滿足。

3.  **通用語言 (Ubiquitous Language)**:
    -   本層的命名（類、方法、屬性）必須與業務專家使用的術語保持一致，形成團隊的通用語言。

## 內部結構 (Internal Structure)

-   `model`: 存放領域模型，包括：
    -   **Aggregate Root**: 聚合的根實體，是外部世界與聚合互動的唯一入口。
    -   **Entity**: 具有唯一標識且生命週期連續的物件。
    -   **Value Object**: 用於描述事物屬性且沒有唯一標識的物件，其核心特徵是**不變性 (Immutability)**。

-   `service`: 存放領域服務 (Domain Service)。
    -   當某個業務邏輯不適合放在任何一個實體或值物件中時（例如，涉及多個聚合的操作），應將其放入領域服務。

-   `repository`: 存放倉儲介面 (Repository Interface)。
    -   定義了聚合的持久化契約，例如 `save`, `findById` 等。
    -   具體的實現由 `infrastructure` 層提供。領域層只依賴介面，不關心底層是 JPA、MyBatis 還是其他存儲技術。

-   `event`: 存放領域事件 (Domain Event)。
    -   當聚合的狀態發生重要變更時，可以發布一個領域事件。
    -   這有助於實現不同業務模組之間的最終一致性和解耦。

**注意**: 任何對 `domain` 層的修改都必須謹慎評估，確保不會引入外部依賴，並維持其業務純粹性。
