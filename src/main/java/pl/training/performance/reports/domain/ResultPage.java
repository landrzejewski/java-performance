package pl.training.performance.reports.domain;

import java.util.List;

public class ResultPage<T> {

    private final List<T> rows;
    private final int totalPages;

    public ResultPage(List<T> rows, int totalPages) {
        this.rows = rows;
        this.totalPages = totalPages;
    }

    public List<T> getRows() {
        return rows;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
