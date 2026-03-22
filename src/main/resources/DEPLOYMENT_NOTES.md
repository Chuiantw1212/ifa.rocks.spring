# 生產環境部署注意事項

本文檔旨在說明在將此 Spring Boot 應用程式部署到像 Google Cloud Run 這樣的反向代理 (Reverse Proxy) 環境時，一個非常關鍵的配置。

## 問題：Swagger UI 在生產環境中無法發出請求

在本地開發時，Swagger UI (`/swagger-ui.html`) 可以正常工作。但是，一旦部署到 Cloud Run，並透過 `https://calculator-api-spring-20260210-592400229145.asia-east1.run.app` 這樣的公開 URL 訪問時，點擊 "Try it out" 會失敗，並在瀏覽器的開發者工具中看到一個關於 CORS 或「混合內容 (Mixed Content)」的錯誤。

### 根本原因：反向代理環境下的「協議 (Protocol)」識別錯誤

這個問題的根源，在於 Cloud Run 的網路環境與 Spring Boot 的預設行為之間的衝突。

讓我們用一個「大廈保全」的比喻來解釋：

1.  **你的 Spring Boot 應用 (大廈內部的管家)**: 他住在一棟與世隔絕的大廈裡，他自己認為，所有進來的訪客，都是從大廈的「普通小門 (HTTP)」進來的。

2.  **Cloud Run (大廈門口的保全)**: 他站在大廈的「豪華正門 (HTTPS)」，負責接待所有貴賓。當一個貴賓 (你的瀏覽器) 透過 HTTPS 來訪時，保全會接待他，然後透過一個內部通道，把他引導到管家面前。

3.  **資訊的丟失**: 在這個引導過程中，保全 **沒有** 告訴管家：「這位貴賓是從『豪華正門 (HTTPS)』進來的。」

4.  **管家的錯誤判斷**: 管家看到這位訪客，因為沒有收到任何特殊通知，所以他理所當然地認為：「哦，這又是一個從『普通小-門 (HTTP)』進來的訪客。」

5.  **「身分危機」導致的錯誤**:
    *   當你的 Spring Boot 應用 (管家) 需要生成一個包含自己完整地址的連結時（例如，在 `/v3/api-docs` 這個 Swagger 定義檔中），它會誠實地在裡面寫上：「我的地址是 `http://...`」，因為它認為自己是在一個不安全的 HTTP 環境中。
    *   當你的瀏覽器 (貴賓) 載入 Swagger UI 頁面後，它讀取了這個定義檔，然後發現：「我要呼叫的 API 地址，竟然是一個不安全的 `http://...`！」
    *   瀏覽器的「混合內容 (Mixed Content)」安全策略被觸發：「**我絕不允許在一個安全的 HTTPS 頁面中，發送一個不安全的 HTTP 請求！**」
    *   因此，瀏覽器直接阻止了這個請求的發送，並在主控台中回報了一個看似是 CORS 問題的錯誤。

## 解決方案：讓「管家」信任「保全」的通知

我們必須修改 `application.yaml`，明確地告訴 Spring Boot：「**嘿，你正運行在一個反向代理 (Cloud Run) 的後面。請你務必信任並採納所有由代理轉發過來的、關於『客戶端真實協議』的標頭 (Headers)！**」

這個標頭，通常叫做 `X-Forwarded-Proto`，Cloud Run 會自動為所有 HTTPS 請求，加上 `X-Forwarded-Proto: https` 這個標頭。

### 具體配置

在 `application.yaml` 中，加入以下設定：

```yaml
server:
  # ... 其他 server 設定 ...
  # 核心修正：告訴 Spring Boot 要信任反向代理 (Cloud Run) 轉發的標頭
  # 這會讓 Spring Boot 知道，即使它自己運行在 HTTP 上，外部請求也是透過 HTTPS 進來的
  forward-headers-strategy: NATIVE
```

這個配置，會從根本上解決 Spring Boot 在 Cloud Run 環境下的「身分危機」，讓它能夠正確地生成所有絕對路徑的 URL，從而修復在生產環境中 Swagger UI 無法使用的問題。
