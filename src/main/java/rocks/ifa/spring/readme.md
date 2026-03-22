
# 🚀 金融複利計算機 API (Calculator API)

這是一個基於 **Spring Boot 3** 構建的高效能金融計算後端。本專案採用 **混合式存儲架構 (Polyglot Persistence)**，結合了關係型資料庫的嚴謹性與 NoSQL 的靈活性，旨在提供精確、可擴展且具備高度配置能力的金融服務。

## 🏗️ 專案套件架構 (Package Structure)

本專案遵循標準的 Spring Boot 分層架構，並針對金融業務需求進行了模組化設計：

```text
com.en_chu.calculator_api_spring
├── config          # 系統配置 (Firebase, MyBatis, Security)
├── constant        # 全域常量與列舉 (如 Gender, CalcType)
├── controller      # RESTful API 接口層
├── dto             # 資料傳輸物件 (Data Transfer Objects)
├── mapper          # MyBatis 介面與資料存取層 (DAO)
├── model           # 核心數據模型 (Database Entities)
└── service         # 業務邏輯與第三方整合 (Firebase Seeding)

```

---

## 💎 核心設計概念：Model vs DTO

在 `com.en_chu.calculator_api_spring` 專案中，我們嚴格區分了 **Model** 與 **DTO**，這是為了確保系統的穩定性與前端顯示的靈活性。

### 1. Model (實體模型)

* **定義**：直接對應 **PostgreSQL** 資料表的結構。
* **職責**：保證數據的一致性與完整性。
* **特點**：欄位嚴謹，通常與資料庫欄位 1:1 對應。
* **範例**：`UserRecord.java` 包含 `id`, `principal`, `gender_code`。

### 2. DTO (資料傳輸物件)

* **定義**：針對 **外部輸入/輸出** (JSON) 進行設計。
* **職責**：將複雜的數據格式化，提供給前端 **Element Plus** 元件使用。
* **特點**：靈活多變，可能包含多個 Model 的組合或額外的 UI 配置。
* **範例**：`OptionConfig.java` 用於處理 Firebase 上的 `opt_gender` 選單配置。

### ⚖️ 差異對照表

| 特性 | **Model (Entity)** | **DTO (Data Transfer Object)** |
| --- | --- | --- |
| **存放路徑** | `com.en_chu.calculator_api_spring.model` | `com.en_chu.calculator_api_spring.dto` |
| **對應目標** | **PostgreSQL** (資料表) | **Firebase / API** (傳輸格式) |
| **欄位變動** | 需配合 SQL Schema，變動成本高。 | 隨 UI 需求快速調整，變動成本低。 |
| **主要功能** | 確保 ACID 事務一致性。 | 簡化 API 接口，隱藏敏感數據。 |

---

## 🗄️ 混合式存儲策略 (Polyglot Persistence)

為了兼顧「穩定性」與「靈活性」，我們採用了不同的資料庫技術：

* **PostgreSQL (SQL)**：存放核心用戶紀錄。利用關係型資料庫的 **ACID** 特性，確保每一筆複利計算紀錄都能精確存儲且不丟失。
* **Firebase Firestore (NoSQL)**：存放 UI 配置（如 `opt_gender`）。這讓前端的 **Element Plus** 選單能具備「零停機更新」的能力，無需更動後端代碼即可修改選單文字或圖示。

---

## 🛠️ 自動化維護工具：Firebase Seeding

專案內建 `FirebaseSeedingService`，支援將本機 `resources/init-data/` 下的 JSON 配置檔自動同步至 Firebase。

1. **Git 版本管理**：所有的選單配置（JSON）皆可進版控。
2. **批次同步**：透過識別 JSON 內的 `id` 自動更新對應的 Firestore Document。
3. **環境一致性**：確保本地開發環境與 Production 環境的選單選項完全同步。

---

## 🚀 快速開始

1. **環境需求**：Java 17+, PostgreSQL 15+, Firebase Service Account Key。
2. **資料初始化**：啟動後調用 `/admin/sync-firebase` 接口，將 `init-data` 同步至 Firebase。
3. **計算 API**：透過 `POST /api/v1/calculate` 執行複利計算並持久化至 PostgreSQL。
