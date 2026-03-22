# 🎮 Controller Layer Architecture

此目錄存放應用程式的 API 入口點 (Entry Points)。
Controller 的主要職責是 **處理 HTTP 請求、驗證參數 (Validation)、路由至對應的 Service**，以及 **封裝回應 (Response)**。

## 🏗️ 核心設計原則 (Design Principles)

為了避免 `UserController` 變成巨大的 God Class，且為了保持程式碼的清晰度，我們採用以下架構原則：

### 1. 職責分離 (Separation of Concerns)
我們根據 **「業務領域」** 與 **「敏感程度」** 將 Controller 拆分：
* **UserController**: 負責一般業務資料的讀取與寫入 (Profile, Career)。
* **UserSecurityController**: 專門負責高風險、高敏感度的操作 (密碼修改、帳號刪除、2FA)。

### 2. 讀寫策略 (Read/Write Strategy)
針對 Service 的調用，我們採用了輕量級的讀寫分離策略：
* **讀取 (READ/Aggregation)**:
    * 透過 `UserService` 進行資料整合。
    * *原因*：讀取 `/me` 時通常需要跨表 Join (User + Profile + Career)，由 `UserService` 統一組裝最合適。
* **寫入 (WRITE/Update)**:
    * Controller **直接呼叫** 子領域 Service (`UserProfileService`, `UserCareerService`)。
    * *原因*：單純的 Profile 更新不需要經過 `UserService` 過手 (避免 Middle Man Code Smell)。

### 3. 安全性 (Security)
* **UID 獲取**: 禁止前端在 Body 傳送 `uid`，一律透過 `SecurityUtils.getCurrentUserUid()` 從 Token 解析。
* **資料隱藏**: 依賴 Entity 的繼承結構 (`UserBaseEntity` + `@JsonIgnore`)，確保回傳 JSON 時自動過濾 `firebaseUid`。

---

## 🛠️ 常用註解與 HTTP 方法對照 (Annotation Guide)

本專案遵循 RESTful 風格，各註解對應的功能如下：

| 註解名稱 | HTTP Method | 用途說明 | 典型場景 |
| :--- | :---: | :--- | :--- |
| @RestController | N/A | **標示控制器**。結合了 @Controller + @ResponseBody，表示此類別專門回傳 JSON 資料。 | 所有 Controller 類別上方 |
| @RequestMapping | N/A | **路徑前綴**。設定該 Controller 下所有 API 的共同路徑。 | 類別上方 (如 /api/v1/user) |
| @GetMapping | GET | **查詢資源**。操作應為 Idempotent (冪等)，不應修改伺服器狀態。 | 取得列表、取得單筆詳情 |
| @PostMapping | POST | **新增資源**。通常不具冪等性，每次呼叫都會建立新資料。 | 新增投資部位、新增房產 |
| @PutMapping | PUT | **更新/置換資源**。針對已知 ID 的資源進行全量或部分更新。 | 更新個人檔案、修改金額 |
| @DeleteMapping | DELETE | **刪除資源**。移除指定的資源。 | 刪除某筆資產 |
| @RequestBody | N/A | **接收 JSON**。將前端傳來的 JSON Body 自動轉為 Java DTO 物件。 | POST/PUT 的參數中 |
| @PathVariable | N/A | **接收路徑參數**。取得網址上的變數 (如 /api/users/{id})。 | DELETE/GET Detail 時 |

---

## 📂 Controller 清單

### 1. UserController
> **Base Path:** `/api/v1/user`
> **描述:** 處理使用者的一般日常資料互動。

| Annotation | Path | Description | Service Strategy |
| :--- | :--- | :--- | :--- |
| @GetMapping | /me | 取得完整個人資料 (Init) | 呼叫 UserService (整合) |
| @PutMapping | /profile | 更新基本資料卡片 | 直接呼叫 UserProfileService |
| @PutMapping | /career | 更新職涯與薪資卡片 | 直接呼叫 UserCareerService |

**程式碼範例:**

```java
@RestController // ✅ 宣告這是一個 REST API 入口
@RequestMapping("/api/v1/user") // ✅ 設定共同路徑
public class UserController {

    @Autowired private UserService userService;          // For GET
    @Autowired private UserProfileService profileService; // For PUT
    @Autowired private UserCareerService careerService;   // For PUT

    /**
     * 取得個人完整資訊
     * HTTP Method: GET
     */
    @GetMapping("/me") 
    public ResponseEntity<UserFullDataRes> getMe() {
        String uid = SecurityUtils.getCurrentUserUid();
        return ResponseEntity.ok(userService.getFullUserData(uid));
    }
    
    /**
     * 更新基本資料
     * HTTP Method: PUT (因涉及資料修改)
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfile> updateProfile(@RequestBody UserProfile req) {
        // @RequestBody: 將前端 JSON 轉為 UserProfile 物件
        String uid = SecurityUtils.getCurrentUserUid();
        return ResponseEntity.ok(profileService.update(uid, req));
    }
}

```

### 2. UserRealEstateController (房地產)

> **Base Path:** `/api/v1/real-estate`

| Annotation | Path | Description | Service Strategy |
| --- | --- | --- | --- |
| @GetMapping | / | 取得房產清單 | UserRealEstateService |
| @PostMapping | / | 新增一筆房產 | UserRealEstateService |
| @PutMapping | /{id} | 更新指定房產 | UserRealEstateService |
| @DeleteMapping | /{id} | 刪除指定房產 | UserRealEstateService |

### 3. UserPortfolioController (投資組合)

> **Base Path:** `/api/v1/user/portfolios`

| Annotation | Path | Description | Service Strategy |
| --- | --- | --- | --- |
| @GetMapping | / | 取得持倉列表 | UserPortfolioService |
| @PostMapping | / | 新增持倉 | UserPortfolioService |
| @PutMapping | /{id} | 更新持倉數值 | UserPortfolioService |
| @DeleteMapping | /{id} | 刪除持倉 | UserPortfolioService |

```

```