package rocks.ifa.client.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> list;
    private long total;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public PageResponse(List<T> list, long total, int currentPage, int pageSize) {
        this.list = list;
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (pageSize == 0) ? 1 : (int) Math.ceil((double) total / pageSize);
    }
}
