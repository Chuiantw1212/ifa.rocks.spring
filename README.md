# 財務計算機後端 API (Calculator API)

本專案是開源財務計算機的後端 API 服務，使用 Java 和 Spring Boot 框架開發，旨在提供一個安全、穩定且可擴展的後端基礎設施。

## 技術架構

本專案採用經典的 RESTful API 分層架構，並由 Spring Boot 進行全自動化的組裝和管理。

```
+----------------+      +-----------------------+      +--------------------------------+      +----------------+
|   前端應用     | ---> |  Google Cloud Run     | ---> |          Spring Boot           | ---> |   PostgreSQL   |
| (Vue/React)    |      | (反向代理, HTTPS)     |      |         (應用程式生產線)       |      |  (Neon DB)     |
+----------------+      +-----------------------+      +--------------------------------+      +----------------+
                                                         |
                                                         |--- 1. Spring Security (安全過濾鏈: CORS, Firebase Auth)
                                                         |--- 2. Spring MVC (Web 引擎: Controller, @RequestMapping)
                                                         |--- 3. Service Layer (業務邏輯)
                                                         |--- 4. MyBatis (資料存取)
```

## 核心技術棧

- **應用程式框架 (Application Framework)**:
  - **Spring Boot 3.3.2**: 作為整個應用程式的「生產線」，負責自動配置、依賴管理和內嵌伺服器，極大地簡化了開發和部署。

- **核心模組 (Core Modules)**:
  - **Spring MVC**: 作為處理 Web 請求的「核心引擎」，提供了 `@RestController`, `@RequestMapping` 等功能。
  - **Spring Security**: 作為應用程式的「安全框架」，負責處理認證、授權和 CORS。
  - **MyBatis 3**: 作為「資料存取框架」，負責將 Java 物件與 SQL 語句進行映射。

- **基礎設施 (Infrastructure)**:
  - **語言**: Java 17
  - **資料庫**: PostgreSQL
  - **認證服務**: Firebase Authentication
  - **API 文件**: SpringDoc (Swagger UI)

---

## API 文件 (Swagger)

本專案使用 SpringDoc 自動生成互動式的 API 文件。

-   **本地開發環境**:
    -   啟動應用程式後，請訪問: [http://localhost:8888/swagger-ui.html](http://localhost:8888/swagger-ui.html)

-   **生產環境**:
    -   部署到 Cloud Run 後，請訪問: [https://calculator-api-spring-20260210-592400229145.asia-east1.run.app/swagger-ui.html](https://calculator-api-spring-20260210-592400229145.asia-east1.run.app/swagger-ui.html)

---

## 套件結構說明

-   `com.en_chu.calculator_api_spring`
    -   `config`: 存放所有 Spring 的 Java 設定檔，負責組裝應用程式的「骨架」。(詳見: `config/README.md`)
    -   `controller`: API 的入口層 (屬於 Spring MVC)，負責接收 HTTP 請求、驗證輸入，並呼叫 Service。
    -   `dto` / `model`: 資料傳輸物件 (Data Transfer Objects)，用於定義 API 的請求和回應的 JSON 結構。
    -   `entity`: 資料庫實體物件，與資料庫中的資料表結構一一對應。
    -   `exception`: 全域例外處理器，負責捕獲運行時錯誤，並回傳統一格式的錯誤訊息。
    -   `mapper`: MyBatis 的 Mapper 介面和 XML 檔案，負責定義和執行實際的 SQL 語句。
    -   `security`: 存放與 Spring Security 和 Firebase 認證相關的類別。
    -   `service`: 業務邏輯的核心層，負責處理所有複雜的業務規則和資料操作。
    -   `util`: 通用工具類，例如從 `SecurityContext` 中獲取當前使用者 UID 的 `SecurityUtils`。

---

## 關鍵設計與安全考量

### 1. 認證機制 (Authentication)

-   本專案採用 **Token-Based Authentication**。
-   前端透過 Firebase SDK 登入後，會獲取一個 **Firebase ID Token**。
-   在呼叫後端 API 時，前端必須將此 Token 放在 HTTP 的 `Authorization` 標頭中，並使用 `Bearer ` 前綴。
-   後端的 `FirebaseTokenFilter` (屬於 Spring Security) 會攔截每一個請求，並使用 Firebase Admin SDK 驗證此 Token 的有效性。只有在驗證成功後，請求才會被放行到後續的 Controller。

### 2. 授權與資料所有權 (Authorization & Data Ownership)

-   **核心原則**: **「使用者永遠只能存取自己的資料。」**
-   **實現方式**: 在所有 Service 層的方法中，我們都嚴格地執行「**UID 檢核**」。在對任何資料進行讀取、更新或刪除之前，SQL 語句的 `WHERE` 條件中，**必須**包含 `firebase_uid = #{firebaseUid}` 這個條件。
-   這確保了即使用戶 A 知道了用戶 B 的某筆資料 ID，他也無法透過 API 來操作不屬於他的資料，從根本上杜絕了橫向越權攻擊。

### 3. 環境變數與密鑰管理 (Secrets Management)

-   **核心原則**: **「任何敏感資訊 (如資料庫密碼、API 金鑰) 都絕不能硬編碼在程式碼中，也絕不能提交到 Git 倉庫。」**
-   **本地開發**: 我們使用一個不受 Git 追蹤的 `application-local.yaml` 檔案來儲存本地的資料庫密碼。
-   **生產環境**: 在部署到 Cloud Run 時，所有敏感資訊都必須設定為**環境變數**。
    -   對於資料庫密碼等最高等級的密鑰，強烈建議使用 **Google Secret Manager** 進行儲存和管理，然後在 Cloud Run 的配置中，將 Secret 的值引用為環境變數。這提供了最高等級的安全性。

### 4. CORS 與反向代理 (Reverse Proxy)

-   在像 Cloud Run 這樣的反向代理環境下，存在一個經典的「協議識別錯誤」問題，可能導致 Swagger UI 或前端應用無法正常發出請求。
-   關於這個問題的詳細解釋和解決方案，請參考專案內的專門技術文件：[**`src/main/resources/DEPLOYMENT_NOTES.md`**](./src/main/resources/DEPLOYMENT_NOTES.md)
