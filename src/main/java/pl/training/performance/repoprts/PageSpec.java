package pl.training.performance.repoprts;

public record PageSpec(int pageNumber, int pageSize) {

    public int getOffset() {
        return pageNumber * pageSize;
    }

}
