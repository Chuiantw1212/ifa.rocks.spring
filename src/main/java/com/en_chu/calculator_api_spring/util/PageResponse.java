package com.en_chu.calculator_api_spring.util;

import java.util.List;

import lombok.Data;

@Data
public class PageResponse<T> {
    private List<T> list;      // 資料本體
    private long total;        // 總筆數
    private int currentPage;   // 當前頁碼
    private int pageSize;      // 每頁筆數
    private int totalPages;    // 總頁數

    public PageResponse(List<T> list, long total, int currentPage, int pageSize) {
        this.list = list;
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (pageSize == 0) ? 1 : (int) Math.ceil((double) total / pageSize);
    }
}