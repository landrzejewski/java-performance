package pl.training.performance.concurrency.ex17_fork_join;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
public class ChangePriceTask extends RecursiveAction {

    private final List<Product> products;
    private final long changeValue;
    private final int startIndex;
    private final int endIndex;
    private final int chunkSize;

    @Override
    protected void compute() {
        if (endIndex - startIndex <= chunkSize) {
            for (int index = startIndex; index <= endIndex; index++) {
                products.get(index).increasePrice(changeValue);
            }
        } else {
            int middle = (startIndex + endIndex) / 2;
            ChangePriceTask firstTask = new ChangePriceTask(products, changeValue, startIndex, middle, chunkSize);
            ChangePriceTask secondTask = new ChangePriceTask(products, changeValue, middle + 1, endIndex, chunkSize);
            invokeAll(firstTask, secondTask);
        }
    }

}
