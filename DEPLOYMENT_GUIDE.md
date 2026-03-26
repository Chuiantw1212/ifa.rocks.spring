# Cloud Run 部署指南 (Spring Boot)

本文件記錄了將此 Spring Boot 應用程式部署到 Google Cloud Run 的標準流程，以及在此過程中可能遇到的常見問題與解決方案。

## 1. Cloud Run 服務配置

在 Google Cloud Console 中建立或配置 Cloud Run 服務時，請遵循以下建議：

### Build Configuration (建構設定)

*   **Build Type (建構類型)**: 選擇 **"Google Cloud's buildpacks"**。
    *   **理由**: 這是最簡單、最安全、也是 Google 推薦的方式。Buildpacks 會自動偵測您的 `pom.xml`，並使用最佳化的方式將您的 Java 專案打包成一個安全的容器映像檔。您無需手動編寫和維護 `Dockerfile`。

*   **Branch (分支)**: `^main$`
    *   設定您的主要部署分支，通常是 `main` 或 `master`。

## 2. 部署流程

Cloud Run 的持續部署 (CD) 流程如下：

1.  **推送程式碼**: 將您的本地變更 `commit` 並 `push` 到您在第一步中設定的目標分支 (例如 `main`)。
2.  **觸發建構**: Cloud Build 會自動偵測到新的推送，並開始一個新的建構任務。
3.  **建構映像檔**: Cloud Build 會使用 Buildpacks 來：
    *   編譯您的 Java 程式碼。
    *   執行 `mvn spring-boot:repackage`。
    *   將產生的 JAR 檔打包成一個安全的容器映像檔。
    *   將這個映像檔推送到 Artifact Registry。
4.  **部署新版本**: Cloud Build 在成功建構映像檔後，會自動將這個新版本部署到您的 Cloud Run 服務，並逐步將流量導向新版本。

## 3. 常見問題與解決方案

### 錯誤: `fork/exec ./mvnw: permission denied`

這是部署 Spring Boot 專案時**最常見**的錯誤之一。

*   **錯誤原因**:
    Cloud Build 在一個基於 Linux 的環境中執行，而 `mvnw` (Maven Wrapper) 是一個 shell 腳本。在 Linux 系統中，腳本檔案必須被明確賦予「可執行」的權限才能被執行。當您在 Windows 系統上開發時，這個權限資訊通常不會被正確地儲存到 Git 中。

*   **解決方案**:
    在您的**本地開發環境**中，打開一個 Git Bash 或任何支援 Linux 命令的終端，然後執行以下命令，為 `mvnw` 檔案加上可執行權限，並將此變更推送到 Git。

    ```bash
    # 1. 為 mvnw 檔案在 Git 索引中加上可執行權限
    git update-index --chmod=+x mvnw

    # 2. 檢查狀態，您會看到類似 "mode change" 的提示
    git status
    # 應該會顯示: changes to be committed: (new file: 100644 => 100755 mvnw)

    # 3. 提交這個權限變更
    git add mvnw
    git commit -m "feat: Add executable permission to mvnw"

    # 4. 推送到遠端倉庫
    git push
    ```

    完成推送後，重新觸發一次 Cloud Build，`permission denied` 的問題就會得到解決。這通常是一個**一次性**的設定。
