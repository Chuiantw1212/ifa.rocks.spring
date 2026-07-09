# ifa.rocks.spring

本專案是基於 Spring Boot 的應用程式，採用 COLA (Clean Object-Oriented and Layered Architecture) 架構思想。

## 架構 (Architecture)

專案遵循六邊形架構（Hexagonal Architecture），也稱為埠與介面卡（Ports and Adapters）架構。其核心目標是將業務核心邏輯與外部技術基礎設施完全隔離，使得業務邏輯（Domain）不依賴任何具體技術實現。

```
+------------------------------------------------------------------------------------+
|                                                                                    |
|  +-----------------+      +-----------------+      +----------------------------+  |
|  |   Web Adapter   |      | Message Adapter |      |      Other Adapters        |  |
|  +-------+---------+      +--------+--------+      +-------------+--------------+  |
|          |                         |                             |                 |
|  +-------v-------------------------v-----------------------------v---------------+  |
|  |                                                                               |  |
|  |                            Application Layer                                  |  |
|  |                                                                               |  |
|  +-------------------------------------+-----------------------------------------+  |
|                                        |                                            |
|  +-------------------------------------v-----------------------------------------+  |
|  |                                                                               |  |
|  |                              Domain Layer                                     |  |
|  |                                                                               |  |
|  +-------------------------------------------------------------------------------+  |
|                                        ^                                            |
|  +-------------------------------------+-----------------------------------------+  |
|  |                                                                               |  |
|  |                         Infrastructure Layer                                  |  |
|  |                                                                               |  |
|  +-------------------------------------------------------------------------------+  |
|                                                                                    |
+------------------------------------------------------------------------------------+
```

### 分層說明

專案的套件結構嚴格遵循分層設計，主要分為以下幾個部分：

-   **`start`**: 應用程式啟動層 (Spring Boot Main Class)
    -   `Application.java`: Spring Boot 啟動類。

-   **`adapter`**: 介面卡層
    -   負責處理與外部系統的互動，並將外部請求轉發給 `application` 層。
    -   `web`: 提供 RESTful API。
    -   `rpc`: 提供 RPC 服務。
    -   `mq`: 處理訊息佇列的生產與消費。

-   **`application`**: 應用層
    -   負責協調 `domain` 層完成業務功能，處理應用程式的 Use Cases。
    -   `service`: 應用服務，編排領域服務與基礎設施。
    -   `dto`: 資料傳輸物件 (Data Transfer Objects)，用於 `adapter` 層與 `application` 層之間的資料交換。
    -   `cqrs`: 如果採用 CQRS 模式，命令 (Commands) 與查詢 (Queries) 會在此定義。

-   **`domain`**: 領域層
    -   業務核心，包含所有業務邏輯與規則，不依賴任何外部框架。
    -   `model`: 領域模型，包含實體 (Entity)、值物件 (Value Object)、聚合根 (Aggregate Root)。
    -   `service`: 領域服務，處理無法歸屬於單一實體的複雜業務邏輯。
    -   `repository`: 倉儲介面，定義領域物件的持久化操作，由 `infrastructure` 層實現。
    -   `event`: 領域事件，用於解耦不同領域模型之間的互動。

-   **`infrastructure`**: 基礎設施層
    -   提供所有技術實現，例如資料庫存取、快取、外部服務呼叫等。
    -   `persistence`: 實現 `domain` 層定義的倉儲介面，例如使用 Spring Data JPA。
    -   `gateway`: 外部服務的客戶端實現。
    -   `config`: 框架與第三方函式庫的組態設定。

## 技術棧 (Technology Stack)

-   **Runtime**: Java / Spring Boot
-   **Persistence**: Spring Data JPA / Hibernate
-   **Relational DB**: Neon DB (PostgreSQL)
-   **NoSQL/Document DB**: Google Firestore

## 如何運行 (How to Run)

1.  **編譯與打包**
    ```bash
    mvn clean install
    ```

2.  **運行應用程式**
    ```bash
    mvn spring-boot:run
    ```
