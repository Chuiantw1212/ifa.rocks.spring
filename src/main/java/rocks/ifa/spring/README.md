# ifa.rocks.spring 專案架構說明

本專案遵循清晰的四層架構，以實現關注點分離 (Separation of Concerns) 與領域驅動設計 (Domain-Driven Design)。

## Adapter (適配層)

這層是系統的門面，負責將外部的 HTTP 或 RPC 請求，轉換為應用層能夠理解的指令。這裡通常放置 Controller、UI 互動邏輯以及 View Object (VO)。它的存在，是為了保護內部邏輯不受外部通訊協定變化的干擾。

## Application (應用層)

這裡是使用案例（Use Case）的編排中心。它不包含核心的業務規則，而是負責接收 Adapter 傳來的指令，協調領域層與基礎設施層來完成任務。這裡會放置 Application Service 以及 Data Transfer Object (DTO)。

## Domain (領域層)

這是系統的心臟，也是最純粹的地方。它封裝了核心業務邏輯，包含實體（Entity）、值物件（Value Object）、聚合根以及領域服務。特別需要注意的是，為了保持領域的純潔性，這裡只會定義基礎設施的「介面」（例如 Repository 介面），而不包含任何具體的技術實作。它不依賴其他任何層級。

## Infrastructure (基礎設施層)

這裡提供具體的技術實現，用來支撐上述所有層級的運作。舉凡資料庫操作（DO 與 Mapper）、外部 API 的呼叫、訊息佇列的收發，都會在這裡實作。它會實作 Domain 層所定義的介面，這正是依賴反轉（Dependency Inversion）的具體展現。
