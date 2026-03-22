# `config` (設定) 套件說明

本套件 (`com.en_chu.calculator_api_spring.config`) 存放了所有與 Spring Boot 應用程式啟動、組裝和基礎設施設定相關的 Java 設定檔。

這些檔案共同定義了應用程式的「骨架」和「行為模式」。

---

## 各設定檔職責詳解

### `AppProperties.java`

*   **核心職責**: 集中管理從 `application.yaml` 讀取的自訂屬性。
*   **運作方式**: 使用 `@ConfigurationProperties` 註解，將 YAML 中 `app.cors` 開頭的屬性，自動綁定到這個 Java 物件的欄位上。這提供了一種型別安全的方式來讀取自訂配置。

### `CorsConfig.java`

*   **核心職責**: 建立 CORS (跨域資源共享) 的規則，決定哪些前端來源 (URL) 可以存取我們的 API。
*   **運作方式**: 它會讀取 `AppProperties` 中的設定值 (例如允許的來源 URL)，並建立一個名為 `corsConfigurationSource` 的 `@Bean`，這個 Bean 會被 `SecurityConfig` 用來套用 CORS 規則。

### `FirebaseConfig.java`

*   **核心職責**: 初始化 Firebase Admin SDK。
*   **運作方式**: 在應用程式啟動時，它會尋找 `service_account_key.json` 這個金鑰檔案，並使用它來初始化一個全域的 `FirebaseApp` 實例。這個實例後續會被用來驗證 Firebase Token。

### `MyBatisConfig.java`

*   **核心職責**: 設定 MyBatis 框架，這是我們用來與資料庫溝通的工具。
*   **運作方式**: 建立 `SqlSessionFactory` 這個核心 `@Bean`，並告訴它去哪裡尋找我們的 SQL 語句定義檔 (即 `classpath:mapper/*.xml`)。

### `SecurityConfig.java`

*   **核心職責**: 設定應用程式的「防火牆」，定義所有 API 的存取權限。這是整個應用程式安全的核心。
*   **運作方式**: 建立一個 `SecurityFilterChain` 的 `@Bean`。我們在這裡使用 `authorizeHttpRequests` 方法，來精準地設定哪個 URL 路徑需要認證、哪個路徑可以公開存取、哪個路徑需要特定角色等。

---

## 核心概念

### Java 設定檔 (`@Configuration`) vs. 屬性檔 (`.yaml`)

*   **Java 設定檔 (`.java`)**: 用來定義 **「如何 (How)」** 建立和組裝應用程式的零件 (Beans)。它們是真正的程式碼，負責處理**邏輯和關係**，例如「A 必須在 B 之後建立」。

*   **屬性檔 (`.yaml`)**: 用來提供 **「是什麼 (What)」** 的設定值。它們是外部化的鍵值對，負責提供可變的數據，例如資料庫的 URL、允許的 CORS 來源等。修改它們通常不需要重新編譯程式碼。

### `@Bean` 與依賴注入

這個套件中的許多方法都被 `@Bean` 標註。這是在告訴 Spring：「請執行這個方法，並將它返回的物件，當作一個『零件』來管理。」Spring 的依賴注入 (DI) 容器會自動處理這些零件之間的依賴關係，例如，在建立 `MyBatisConfig` 中的 `SqlSessionFactory` 之前，它會確保 `DataSource` (資料庫連線池) 這個零件已經被建立好。
