# 核心後端模組 (Spring Boot CRUD 核心系統)

本模組為基於 Spring Boot 3.4+ 架構建構的企業級後端系統，核心設計圍繞於高吞吐量、強型別安全與長期可維護性，全面防禦常見的架構腐化與效能技術債。

## 架構邊界與目錄規範

專案揚棄傳統按技術分層打包的弱內聚結構，全面推行按領域或功能打包 (Package by Feature/Domain) 思維。屬於同一業務領域的控制器、服務、持久層與傳輸物件皆封裝在同一套件中，透過 Java 的 package-private 存取修飾子隱藏內部實作，為未來向微服務拆分奠定物理邊界。

### 套件目錄結構示範

```text
src/main/java/com/example/myapp
├── Application.java (主程式入口)
├── common (全域橫切關注點配置)
│   ├── config (基礎設施配置)
│   ├── exception (全域例外轉譯處理)
│   └── logging (日誌與監控指標攔截)
├── user (使用者管理領域)
│   ├── UserController.java (展示層)
│   ├── UserService.java (業務介面)
│   ├── UserServiceImpl.java (業務實作)
│   ├── UserRepository.java (持久層)
│   └── UserRecord.java (不可變 DTO)
└── order (訂單處理領域)
    ├── OrderController.java
    ├── OrderService.java
    ├── OrderRepository.java
    └── OrderRecord.java

```

### 架構分層職責說明

* 展示層 (Presentation Layer)
處理 HTTP 動詞、定義 URI、反序列化 JSON 負載並執行基礎參數驗證。控制器必須保持極度輕量，嚴禁滲透任何業務決策。
* 業務層 (Business Service Layer)
實作核心領域規則與跨模組協調，是系統的核心大腦。本層為定義 @Transactional 資料庫交易邊界的唯一場所。控制層僅能依賴服務層介面，以符合依賴反轉原則並便利單元測試的替身物件注入。
* 持久層 (Persistence Repository Layer)
透過 Spring Data JpaRepository 封裝所有資料庫存取細節，提供獨立於底層實作的抽象介面，精準將關聯式資料映射為 Java 領域物件。
* 介接層 (Data Transfer Object)
強制引入 DTO 隔離內部 JPA 實體與外部 API 契約。全面採用 Java Record 實現不可變性，消除 Setter 濫用帶來的副作用，並利用 JVM 底層機制降低垃圾回收負擔，提升高併發場景的吞吐量。

## 核心技術選型與實務演進

### 1. 物件映射引擎 (MapStruct)

本項目拒絕使用仰賴執行期反射機制的 ModelMapper 或 Dozer，以規避嚴重的執行期效能損耗與缺乏編譯期型別安全的隱患。全面採用基於 JSR 269 標準的 MapStruct 註解處理器，在專案編譯階段生成純 Java 方法調用，具備等同於手寫程式碼的極致效能與編譯期錯誤檢測能力。

* 針對部分更新 (HTTP PATCH) 請求，必須配置 nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE，確保傳入的 null 欄位不會意外覆寫資料庫既有資料。

### 2. 標準化響應與錯誤處理 (RFC 9457)

系統引入對 IETF RFC 9457 規範的原生支援，透過 spring.mvc.problemdetails.enabled=true 配置，將全域例外自動轉譯為標準化的問題詳細資訊 (Problem Details) JSON 格式。

* 自訂業務例外（如資源未找到）應透過 ProblemDetail.forStatusAndDetail() 建構響應，以提供機器可識別且富含上下文的錯誤指引。

### 3. 持久層深層效能調優

* 禁用 OSIV 反模式：application.properties 必須明確宣告 spring.jpa.open-in-view=false，嚴禁將資料庫連線鎖定至整個 HTTP 請求週期，從根本上防禦連線池災難性耗盡與神不知鬼不覺的 N+1 查詢。
* N+1 與分頁處理：針對關聯資料載入，於 JPQL 中使用 JOIN FETCH 或 @EntityGraph 一次性映射至記憶體。若遇到 JOIN FETCH 與 Pageable 分頁機制的衝突，嚴禁觸發 In-Memory Pagination 以免引發記憶體溢出，必須採用兩階段抓取策略 (Two-Phase Fetching) 進行優化。
* 集合型別博弈：在雙向 @OneToMany 關聯中，優先選用 java.util.List 作為容器。避免使用 Set 導致瞬態實體在未獲取資料庫生成 ID 前計算 hashCode() 時產生的 HashSet 線性退化技術陷阱。
* 投影查詢 (Projections)：唯讀情境下，優先在 @Query 中利用 JPQL 建構子語法將結果直接映射至自訂的 Record DTO 中，實現 SQL 層面的極致資料瘦身。

### 4. 分散式追蹤與可觀測性

引進映射診斷上下文 (MDC) 機制以增強日誌品質。透過 Micrometer Observation API 與 Actuator 模組自動橋接 OpenTelemetry，為每個請求建立 Trace 與 Span 並自動注入 MDC 供 Logback 日誌輸出。

* 開發與排錯階段，可透過配置 management.tracing.sampling.probability=1.0 進行百分之百採樣。
* 注意：為防止重用執行緒池引發上下文錯亂與記憶體洩漏，必須在過濾器或攔截器的 finally 區塊或 afterCompletion 階段確實執行 MDC.clear()。

### 5. 防禦性測試策略

遵循測試金字塔與 FIRST 原則，拒絕濫用會載入完整上下文的 @SpringBootTest。

* 上下文切片測試：善用 @WebMvcTest、@DataJpaTest 與 @JsonTest 進行輕量化的層級隔離驗證。
* 高保真集成測試：全面揮別 H2 記憶體資料庫的虛假承諾，引入 Testcontainers 於 Docker 中運行與生產環境一致的真實資料庫（如 PostgreSQL）。透過 @ServiceConnection 註解實現自動化配置，並採用單例容器模式 (Singleton Container Pattern) 維持極速的整合測試執行流。
* AOT 編譯相容：因應 Spring Boot 3.4+ 對 Ahead-Of-Time 編譯與 GraalVM 原生鏡像的演進趨勢，專案中全面停用已廢棄的 @MockBean 與 @SpyBean，改為使用與 AOT 完美相容的 @MockitoBean 與 @MockitoSpyBean。