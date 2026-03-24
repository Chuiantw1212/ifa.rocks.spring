package rocks.ifa.spring.infra.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> list;
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public PageResponse(List<T> list, long totalElements, int currentPage, int pageSize) {
        this.list = list;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (pageSize == 0) ? 1 : (int) Math.ceil((double) totalElements / pageSize);
    }
}
