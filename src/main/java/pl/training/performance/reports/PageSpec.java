package pl.training.performance.reports;

public record PageSpec(int pageNumber, int pageSize) {

    public int getOffset() {
        return pageNumber * pageSize;
    }

}
