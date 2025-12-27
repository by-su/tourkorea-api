package response;

import lombok.Data;

@Data
public class Page {
    private int totalPage;
    private long totalCount;

    public Page(int totalPage, long totalCount) {
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }
}
