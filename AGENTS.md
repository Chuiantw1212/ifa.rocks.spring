# Spring Boot & Neon PostgreSQL 開發規範指南 (AGENTS.md)

本文件定義了 AI Agent 在生成、修改或審查基於 Spring Boot (3.x) 與 Neon (PostgreSQL) 專案程式碼時，必須絕對遵守的核心原則與架構標準。請在撰寫程式碼前載入並嚴格遵守以下規則。

## 1. 專案結構與架構邊界

* **套件命名原則**：必須採用「按領域打包 (Package by Feature/Domain)」，而非按技術分層打包。將同一領域的 Controller、Service、Repository、DTO 組織在同一個 Package 下，並善用 Java 的 package-private 存取級別來隱藏內部實作。


* **介面隔離**：Service 層必須採用「介面 (Interface) + 實作類別 (Impl)」模式。Controller 僅能依賴 Service 介面，以符合依賴反轉原則。


* **單向依賴**：嚴格遵守 Controller -> Service -> Repository 的單向呼叫，嚴禁跨層或逆向呼叫。



## 2. 資料傳輸與物件映射 (DTO & Mapping)

* **實體隔離 (Entity Isolation)**：絕對禁止將 JPA Entity 直接暴露於 Controller 層或作為 API 響應回傳。必須強制使用 DTO 進行隔離。


* **使用 Java Record**：網路傳輸層的 DTO 必須優先使用 Java 14+ 的 Record 型別，以確保不可變性 (Immutability)。


* **映射工具**：強制使用 MapStruct (Compile-time generation) 進行 Entity 與 DTO 之間的轉換，嚴禁使用 ModelMapper 等基於執行期反射的工具。


* **MapStruct 配置**：
* 必須設定 `componentModel = "spring"` 讓其成為 Spring 容器管理的 Bean。


* 在處理 HTTP PATCH 部分更新時，必須配置 `nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE`，防止 null 值覆寫現有資料。





## 3. RESTful API 設計與例外處理

* **HTTP 動詞語意化**：嚴格區分 GET (讀取)、POST (建立)、PUT (完整替換)、PATCH (部分更新)、DELETE (刪除)。URI 路徑中嚴禁包含動詞。


* **標準化例外響應**：基於 Spring Boot 3，全面採用 RFC 9457 ProblemDetail 規範進行全域例外處理。在 `@RestControllerAdvice` 中，使用 `ProblemDetail.forStatusAndDetail()` 來建構標準化錯誤響應，放棄舊有的泛型 Wrapper。



## 4. Neon PostgreSQL 與持久層 (JPA/Hibernate) 防坑配置

* **Neon 連線字串雙軌制**：
* Spring Boot 應用程式 (Hibernate/JPA) 必須使用帶有 `-pooler` 的 Pooled 連線字串，以利用 PgBouncer 連線池並避免耗盡資料庫連線。


* Flyway 或 Liquibase 等 Schema 遷移工具，必須使用 Direct 連線字串（無 `-pooler`），因為遷移作業不支援 transaction pool 模式。




* **關閉 Prepared Statements**：因 Neon 的 PgBouncer 運行於 transaction 模式，不支援跨連線的預先編譯語句。Spring Boot 的 JDBC URL 結尾必須強制加上 `?prepareThreshold=0`，否則 Hibernate 執行時將會報錯。


* **處理 Neon 冷啟動 (Cold Start)**：為因應 Neon Scale-to-zero 的喚醒延遲，必須將 Spring Boot 中的 HikariCP `connectionTimeout` 設置為至少 10000 到 15000 毫秒（10-15秒）。針對瞬態連線失敗 (如 SQLSTATE 57P01)，應在 Service 層實作指數退避的重試機制。


* **關閉 OSIV**：確保 `application.properties` 中明確設定 `spring.jpa.open-in-view=false`。嚴禁依賴 Open Session In View 進行延遲載入。


* **預防 N+1 查詢**：存取關聯資料時，必須在 Repository 透過 `@Query` 搭配 `JOIN FETCH` 進行預先抓取。


* **分頁與 JOIN FETCH 衝突**：若同時需要分頁 (`Pageable`) 與關聯抓取，嚴禁在單一查詢中同時使用 `JOIN FETCH` 與分頁。必須採用「先分頁查詢 ID，再透過 ID 進行 JOIN FETCH」的兩階段查詢。


* **雙向關聯集合型別**：配置雙向的 `@OneToMany` 關聯時，優先使用 `java.util.List` 而非 `java.util.Set`，以提升新增元素時的效能。



## 5. 可觀測性與日誌記錄 (Logging & Observability)

* **MDC (Mapped Diagnostic Context)**：必須在請求最前端 (Filter 或 Interceptor) 注入全域唯一的 `traceId`，並透過 MDC 寫入上下文。執行緒結束前務必呼叫 `MDC.clear()` 防止記憶體洩漏。


* **自動化分散式追蹤**：微服務通訊應使用 Micrometer Tracing 搭配 OpenTelemetry (OTLP) 模組，讓 Spring Boot 自動傳遞 W3C Trace 標頭並處理 Trace ID。



## 6. 測試驅動與自動化 (Testing)

* **測試切片 (Test Slices)**：避免濫用 `@SpringBootTest`。Controller 測試使用 `@WebMvcTest`，Repository 測試使用 `@DataJpaTest`。


* **Mock 依賴遷移**：Spring Boot 3.4.0 起，嚴禁使用已廢棄的 `@MockBean` 與 `@SpyBean`。強制使用來自 `spring-test` 模組的 `@MockitoBean` 與 `@MockitoSpyBean` 以相容 AOT 編譯。


* **真實資料庫測試**：整合測試必須廢棄 H2 記憶體資料庫，全面導入 Testcontainers。利用 Spring Boot 3.1+ 的 `@ServiceConnection` 註解自動綁定容器與資料庫連線，並採用單例容器模式 (Singleton Container) 共用 PostgreSQLContainer 加速測試執行。