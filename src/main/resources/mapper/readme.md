# 📦 Mapper Package (Data Access Layer)

這個套件 (Package) 是本專案的 **資料存取層 (Data Access Layer)**。
本專案不使用 JPA (Repository) 模式，而是採用 **MyBatis** 框架，透過 **Java Interface** 定義方法，並搭配 **XML** 撰寫實際的 SQL 邏輯。

## 📂 目錄結構與對應 (Project Structure)

MyBatis 採用 **「介面 (Interface)」** 與 **「實作 (XML)」** 分離的架構。雖然它們物理位置不同，但邏輯上是緊密綁定的：

```text
src
├── main
│   ├── java
│   │   └── com.en_chu.calculator_api_spring.mapper
│   │       ├── CalculationMapper.java  (👉 Java 介面：定義方法簽章)
│   │       └── UserProfileMapper.java
│   │
│   └── resources
│       └── mapper
│           ├── CalculationMapper.xml   (👉 XML 設定：撰寫 SQL 實作)
│           └── UserProfileMapper.xml

```

---

## 🛡️ SQL 撰寫規範 (Formatting & CDATA)

為了避免 Eclipse/IntelliJ 的自動格式化工具 (Auto Formatter) 破壞 SQL 的縮排與結構，本專案採行 **金融級防禦寫法**：

### 必須使用 `CDATA` 包覆 SQL

所有在 XML 中的 SQL 語句，**必須** 包在 `<![CDATA[ ... ]]>` 區塊中。

**✅ 正確範例：**

```xml
<select id="findAllRecords" resultType="...">
    <![CDATA[
        SELECT * FROM calculation_records 
        WHERE created_at > CURRENT_DATE - 7
        ORDER BY id DESC
    ]]>
</select>

```

**好處：**

1. **防止排版崩壞**：IDE 通常不會格式化 CDATA 內的純文字，確保手動排好的階梯狀縮排不會被壓扁。
2. **特殊符號保護**：SQL 中若包含 `<` (小於)、`>` (大於) 或 `&` 等符號，不需要轉義 (如 `&lt;`)，直接寫即可。

---

## 🔗 MyBatis 綁定鐵律 (Binding Rules)

若發生 `Invalid bound statement (not found)` 錯誤，請優先檢查以下 **三大綁定規則** 是否完全吻合：

### 1. Namespace 綁定 (最重要的路徑)

XML 根節點的 `namespace` 必須完全等於 **Java Interface 的完整路徑 (Fully Qualified Name)**。

* **Java**: `package com.en_chu.calculator_api_spring.mapper;`
* **XML**: `<mapper namespace="com.en_chu.calculator_api_spring.mapper.CalculationMapper">`

### 2. Method ID 綁定

XML 標籤的 `id` 屬性必須完全等於 **Java Interface 的方法名稱**。

* **Java**: `void insertRecord(...)`
* **XML**: `<insert id="insertRecord" ...>`

### 3. ResultType 綁定

若 SQL 為查詢 (`SELECT`)，必須指定回傳的資料型態 (通常為 Model/DTO 的完整路徑)。

* **XML**: `resultType="com.en_chu.calculator_api_spring.model.CompoundInterestReq"`

---

## 📝 現有 Mapper 列表

| Mapper 名稱 | 對應 Entity/Model | 主要功能 | SQL 特色 |
| --- | --- | --- | --- |
| **`CalculationMapper`** | `CompoundInterestReq` | 存取複利計算歷史紀錄 | 基礎 CRUD |
| **`UserProfileMapper`** | `UserProfile` | 存取使用者個人資料 | 使用 PostgreSQL `ON CONFLICT` 實作 Upsert (存在即更新) |

---

## 🔧 常見錯誤排除 (Troubleshooting)

### Q1: 修改了 XML 但重啟後沒生效？

* **原因**: Eclipse 有時候沒把 `src/main/resources` 的更動同步到 `target/classes`。
* **解法**: 對專案按右鍵 -> **Maven** -> **Update Project**，或執行 `Project -> Clean`。

### Q2: 縮排還是跑掉？

* **解法**: 請確認 Eclipse 設定 `Preferences -> XML -> XML Files -> Editor -> Formatting` 中的 **"Preserve whitespace in tags with PCDATA content"** 已勾選，並且 SQL 確實包在 `CDATA` 中。

### Q3: 為什麼沒有 `Repository` 套件？

* **說明**: 本專案使用 MyBatis (`Mapper` 模式)，不使用 Spring Data JPA (`Repository` 模式)。兩者功能重疊，為保持架構精簡，**請勿建立 Repository 套件**。