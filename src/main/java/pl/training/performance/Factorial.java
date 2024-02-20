package pl.training.performance;

import java.util.stream.LongStream;

public class Factorial {

    public long factorialWithLoop(int n) {
        long result = 1;
        for (int index = 2; index <= n; index++) {
            result = result * index;
        }
        return result;
    }

    public long factorialWithStreams(int n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (long result, long index) -> result * index);
    }

    public long factorialWithRecursion(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorialWithRecursion(n - 1);
    }

}
