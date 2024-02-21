package pl.training.performance.concurrency.ex17_fork_join;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Product {

    @NonNull
    private long price;

    public void increasePrice(long changeValue) {
        price += changeValue;
    }

}
