# IFA.Rocks Spring Boot 後端專案

## 專案簡介

IFA.Rocks 是一個為獨立財務顧問 (IFA) 設計的平台，旨在提供強大的後端支持，協助顧問管理客戶資料、進行財務規劃分析、以及自動化日常營運。本專案是其後端服務的核心，採用 Spring Boot 框架開發，並部署於 Google Cloud Run。

## 核心技術棧

*   **後端框架**: Spring Boot 3.x
*   **語言**: Java 17+
*   **資料庫**: PostgreSQL (透過 Flyway 進行版本控制)
*   **ORM**: Spring Data JPA / Hibernate
*   **認證**: Firebase Authentication (透過 Firebase Admin SDK)
*   **雲端服務**: Google Cloud Run, Google Cloud Firestore
*   **API 文件**: Springdoc OpenAPI (Swagger UI)
*   **監控**: Spring Boot Actuator / Micrometer

## 專案架構：領域驅動設計 (Domain-Driven Design, DDD)

本專案嚴格遵循領域驅動設計 (DDD) 的原則，旨在建立一個高內聚、低耦合、易於理解和擴展的業務系統。

### 核心概念

1.  **領域 (Domain)**: 每個頂層套件 (例如 `agent`, `client`, `metadata`) 代表一個獨立的業務領域。每個領域都包含其專屬的業務邏輯、資料模型和 API 契約。
2.  **聚合 (Aggregate)**: 每個領域的核心實體 (例如 `Client` 聚合了 `ClientProfile`, `ClientCareer` 等) 被視為一個聚合根 (Aggregate Root)，確保資料的一致性。
3.  **實體 (Entity)**: 具有唯一標識符和生命週期的業務對象 (例如 `ClientProfileEntity`)。
4.  **值對象 (Value Object)**: 描述性對象，沒有唯一標識符，其相等性基於其屬性值 (例如 DTOs)。
5.  **服務 (Service)**:
    *   **領域服務 (Domain Service)**: 處理跨多個實體或值對象的業務邏輯。
    *   **應用服務 (Application Service)**: 協調領域服務和基礎設施服務，處理用例 (use case) 流程。
6.  **倉儲 (Repository)**: 負責實體的持久化和檢索，將領域層與基礎設施層的資料庫細節解耦。
7.  **基礎設施層 (Infrastructure)**: 處理技術細節，如資料庫連接、外部服務整合 (Firebase, Firestore)、安全配置、通用工具等。

### 目錄結構概覽

```
src/main/java/rocks/ifa/spring/
├── domain/                  # 領域層：核心業務邏輯
│   ├── agent/               # 顧問管理領域
│   │   ├── contracts/       # 顧問相關 DTOs (LoginReq, AgentRes 等)
│   │   ├── AgentController.java
│   │   ├── AgentService.java
│   │   └── AgentServiceImpl.java
│   ├── client/              # 客戶聚合領域
│   │   ├── contracts/       # 客戶聚合 DTOs (CreateClientReq, ClientFullDataRes 等)
│   │   ├── ClientController.java
│   │   ├── ClientService.java
│   │   └── ClientServiceImpl.java
│   ├── clientCareer/        # 客戶職涯子領域
│   │   ├── contracts/       # 職涯相關 DTOs
│   │   ├── ClientCareerService.java
│   │   └── ClientCareerServiceImpl.java
│   ├── ... (其他客戶子領域，如 clientLaborInsurance, clientLaborPension 等)
│   └── metadata/            # 元數據管理領域
│       ├── contracts/       # 元數據相關 DTOs (LifeExpectancyRes 等)
│       ├── MetadataController.java
│       ├── MetadataService.java
│       └── MetadataServiceImpl.java
└── infra/                   # 基礎設施層：技術細節與通用工具
    ├── config/              # Spring 配置 (SecurityConfig, FirestoreConfig 等)
    ├── exception/           # 全局異常處理
    ├── security/            # 安全相關 (FirebaseTokenFilter, SecurityUtils 等)
    ├── status/              # 伺服器狀態檢查
    └── common/              # 通用工具與 DTOs (PageResponse, ClientBaseEntity 等)
```

## 部署資訊

本專案已部署至 Google Cloud Run。

*   **專案網址**: [https://ifa.rocks/](https://ifa.rocks/)
*   **Swagger UI (API 文件)**: [https://ifa-rocks-899902006292.asia-east1.run.app/swagger-ui/index.html#/](https://ifa-rocks-899902006292.asia-east1.run.app/swagger-ui/index.html#/)
*   **伺服器狀態檢查**: [https://ifa-rocks-899902006292.asia-east1.run.app/](https://ifa-rocks-899902006292.asia-east1.run.app/)

## 本地運行

1.  **環境準備**:
    *   Java 17+
    *   Maven 3.x
    *   PostgreSQL 資料庫
    *   Google Cloud SDK (用於 Firebase 和 Firestore 模擬器或服務帳戶)
    *   Firebase 服務帳戶金鑰 (`serviceAccountKey.json`)，放置於 `src/main/resources/`。

2.  **資料庫配置**:
    *   在 `application.properties` 中配置您的 PostgreSQL 連接資訊。
    *   Flyway 會在應用程式啟動時自動執行資料庫遷移。

3.  **運行應用程式**:
    ```bash
    mvn spring-boot:run
    ```
    或在您的 IDE 中直接運行 `SpringApplication` 主類別。

## 貢獻

歡迎對本專案提出貢獻。請遵循以下步驟：
1.  Fork 本專案。
2.  建立您的功能分支 (`git checkout -b feature/AmazingFeature`)。
3.  提交您的變更 (`git commit -m 'Add some AmazingFeature'`)。
4.  推送到分支 (`git push origin feature/AmazingFeature`)。
5.  開啟一個 Pull Request。

---
