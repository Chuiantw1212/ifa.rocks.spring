# System Initialization Data & Options

本文件記錄系統中通用的選項配置（Options Configuration）。這些配置通常用於前端的下拉選單 (Select)、選項按鈕 (Radio) 以及後端的資料驗證標準。

## 1. 資料結構標準 (Data Schema)

所有的選項配置應遵循以下 JSON 結構，以確保前後端處理邏輯的一致性。

### 核心物件結構

| 欄位 | 型別 | 說明 | 範例 |
| --- | --- | --- | --- |
| **id** | String | 設定檔唯一識別碼 (Key)，通常以 `opt_` 開頭 | `opt_group_id` |
| **name** | String | 設定檔中文名稱 (開發者識別用) | `分組標籤配置` |
| **list** | Array | 選項清單 | `[...]` |

### List 選項結構

| 欄位 | 型別 | 說明 | 備註 |
| --- | --- | --- | --- |
| **code** | String/Number | **存入資料庫的值** (DB Value) | **不可重複**。用於後端儲存與邏輯判斷。 |
| **label** | String | **前端顯示文字** (UI Label) | 用於使用者介面顯示。 |
| **sort** | Number | 排序權重 | 數字越小越靠前。 |
| **icon** | String | (選填) 對應前端 Icon Component 名稱 | 例如 `Male`, `Plus`。 |
| **color** | String | (選填) Hex Color Code | 用於標籤或圖表顏色渲染。 |

---

## 2. 現有配置清單 (Current Configurations)

### 2.1 事業分組標籤 (Business Group)

* **ID**: `opt_group_id`
* **用途**: 用於 `user_businesses` 資料表中的 `group_id` 欄位。使用者可自定義將事業歸類為不同顏色群組。
* **資料庫型別**: `INTEGER`

```json
{
  "id": "opt_group_id",
  "name": "分組標籤配置",
  "list": [
    { "code": 1, "label": "藍", "color": "#3B82F6", "sort": 1 },
    { "code": 2, "label": "橘", "color": "#F97316", "sort": 2 },
    { "code": 3, "label": "紫", "color": "#8B5CF6", "sort": 3 },
    { "code": 4, "label": "青", "color": "#06B6D4", "sort": 4 },
    { "code": 5, "label": "粉", "color": "#EC4899", "sort": 5 }
  ]
}

```

### 2.2 性別選項 (Gender)

* **ID**: `opt_gender`
* **用途**: 用於使用者個人資料設定。
* **資料庫型別**: `VARCHAR` / `CHAR`

```json
{
  "id": "opt_gender",
  "name": "性別選項配置",
  "list": [
    { "code": "MALE", "label": "男性", "icon": "Male", "sort": 1 },
    { "code": "FEMALE", "label": "女性", "icon": "Female", "sort": 2 }
  ]
}

```

### 2.3 稅務類別 (Tax Category)

* **ID**: `opt_tax_category`
* **用途**: 用於 `user_businesses` 計算稅務邏輯。
* **資料庫型別**: `VARCHAR`

```json
{
  "id": "opt_tax_category",
  "name": "稅務申報類別",
  "list": [
    { 
      "code": "deemed_6", 
      "label": "擴大書審 (6%)", 
      "desc": "適用於年營收較低，直接以純益率 6% 計算",
      "sort": 1 
    },
    { 
      "code": "verified", 
      "label": "查帳申報 (核實)", 
      "desc": "需完整保留憑證，核實扣除成本",
      "sort": 2 
    },
    { 
      "code": "exempt", 
      "label": "免稅 / 其他", 
      "desc": "如個人免稅額度內或其他特殊狀況",
      "sort": 3 
    }
  ]
}

```

---

## 3. 開發指引 (Implementation Guide)

### 前端 (Vue.js)

建議建立一個共用的 Constant 檔案或是 Store 來管理這些設定，避免在各個 Page 裡寫死 (Hardcode)。

**範例：`src/constants/options.js**`

```javascript
export const SYSTEM_OPTIONS = {
  opt_group_id: [
    { code: 1, label: '藍', color: '#3B82F6' },
    // ...
  ],
  opt_tax_category: [
    { code: 'deemed_6', label: '擴大書審 (6%)' },
    // ...
  ]
}

// Helper: 根據 code 取得 label
export function getOptionLabel(key, code) {
  const option = SYSTEM_OPTIONS[key]?.find(item => item.code === code);
  return option ? option.label : code;
}

```

### 後端 (Spring Boot)

1. **DTO 驗證**：使用 `@Pattern` (針對字串) 或 `@Min/@Max` (針對數字) 來確保前端傳來的 `code` 是合法的。
2. **Enum 定義 (選用)**：如果業務邏輯複雜，建議在 Java 建立對應的 `Enum`。

**範例：DTO Validation**

```java
// 針對 opt_group_id (Integer)
@Min(1) @Max(5)
private Integer groupId;

// 針對 opt_tax_category (String)
@Pattern(regexp = "^(deemed_6|verified|exempt)$")
private String taxCategory;

```

---

## 4. 如何新增一組設定？

1. **定義 JSON**：依照本文第 1 節的結構，定義新的 JSON。
2. **資料庫欄位**：
* 若 `code` 為數字，請開 `INTEGER` 欄位。
* 若 `code` 為字串，請開 `VARCHAR` 欄位。


3. **前端更新**：將 JSON 加入前端的設定檔 (`options.js`)。
4. **後端更新**：在 DTO 加入欄位並設定驗證註解。
5. **文件更新**：將新的 JSON 定義更新回此 `README.md`，保持文件最新。