# 📦 Mapper Package (Data Access Layer)

這個套件 (Package) 是本專案的 **資料存取層 (Data Access Layer)**，負責處理所有與 PostgreSQL 資料庫的溝通。
本專案採用 **MyBatis** 框架，透過 **Java Interface (介面)** 定義方法，並視情況結合 **XML 配置** 或 **Annotation** 來執行 SQL 語句。

## 📂 目錄結構與對應關係

在 MyBatis 架構中，Mapper 分為兩個部分，存放於不同的目錄，但必須維持嚴格的對應關係：

```text
src
├── main
│   ├── java
│   │   └── com.en_chu.calculator_api_spring.mapper
│   │       ├── CalculationMapper.java  (👉 菜單：定義有哪些方法可呼叫)
│   │       └── UserProfileMapper.java
│   │
│   └── resources
│       └── mapper
│           └── UserProfileMapper.xml   (👉 食譜：定義實際的 SQL 邏輯)

```

## 🛠️ 開發規範 (Development Guidelines)

### 1. 命名規則 (Naming Convention)

* **介面名稱**：以 `Mapper` 結尾，例如 `UserProfileMapper`。
* **方法名稱**：應具備語義，例如：
* `insert...`: 新增
* `update...`: 更新
* `upsert...`: 新增或更新 (PostgreSQL `ON CONFLICT` 語法)
* `select...` / `find...`: 查詢
* `delete...`: 刪除



### 2. XML 對應鐵律 (The Golden Rules)

若使用 XML 撰寫 SQL (如 `UserProfileMapper`)，必須遵守以下 **三大綁定規則**，否則會噴出 `Invalid bound statement` 錯誤：

1. **Namespace 綁定**：
XML 根節點的 `<mapper namespace="...">` 必須完全等於 **Java Interface 的完整路徑 (Fully Qualified Name)**。
```xml
<mapper namespace="com.en_chu.calculator_api_spring.mapper.UserProfileMapper">

```


2. **ID 綁定**：
XML 標籤的 `id` 屬性必須完全等於 **Java Interface 的方法名稱**。
```java
// Java
int upsertProfile(UserProfile profile);

```


```xml
<insert id="upsertProfile"> ... </insert>

```


3. **參數與回傳型別**：
* **參數 (`parameterType`)**: 通常可省略，MyBatis 會自動推斷。若傳入物件，SQL 中使用 `#{屬性名}` 取值。
* **回傳 (`resultType`)**: `SELECT` 查詢必填，指定將結果映射為哪個 Entity 或 DTO。



## 📝 現有 Mapper 列表

| Mapper 名稱 | 對應 Entity | 實作方式 | 用途說明 |
| --- | --- | --- | --- |
| **`CalculationMapper`** | `CompoundInterestReq` | Annotation / XML | 負責存取複利計算的歷史紀錄 (`calculation_records`)。 |
| **`UserProfileMapper`** | `UserProfile` | **XML** | 負責存取使用者個人資料 (`user_profiles`)。使用 `ON CONFLICT` 語法實作 Upsert 邏輯。 |
| **`DataAdminMapper`** | N/A | **XML** | 負責執行管理任務，例如在啟動時清除孤兒資料。 |

## 🔧 常見問題排除 (Troubleshooting)

### ❌ 錯誤：Invalid bound statement (not found)

這是 MyBatis 最經典的錯誤，代表 Java 找不到對應的 XML SQL。請依照以下順序檢查：

1. **檢查 `application.yaml` 設定**：
確認是否已指定 XML 路徑：
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml

```


2. **檢查 Namespace**：
XML 裡的 `namespace` 複製貼上到 Java 檔確認是否只有一個字元不同。
3. **檢查檔案編譯**：
有時候 Eclipse/Maven 沒有把 XML 複製到 `target/classes` 目錄。嘗試執行 `Maven Update` 或 `Clean & Build`。
4. **檢查 XML 檔名與路徑**：
確認 XML 檔案確實位於 `src/main/resources/mapper/` 資料夾下。

---

## 💡 SQL 撰寫技巧 (PostgreSQL Specific)

本專案使用 PostgreSQL，以下是一些常用的特殊語法範例：

### Upsert (存在即更新，不存在即寫入)

在 `UserProfileMapper.xml` 中使用了此技巧：

```sql
INSERT INTO table_name (...) VALUES (...)
ON CONFLICT (id) 
DO UPDATE SET column = EXCLUDED.column, ...;

```

### JSONB 處理 (如有需要)

MyBatis 需搭配 TypeHandler 才能完美處理 PostgreSQL 的 JSONB 欄位，目前專案尚未啟用此功能，暫以 String 或拆分欄位處理。