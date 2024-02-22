package pl.training.performance.reports.domain;

public record PageSpec(int pageNumber, int pageSize) {

    public int getOffset() {
        return pageNumber * pageSize;
    }

}
