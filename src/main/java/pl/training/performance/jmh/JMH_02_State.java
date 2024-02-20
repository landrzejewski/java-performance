package pl.training.performance.jmh;

import org.openjdk.jmh.annotations.*;

public class JMH_02_State {

    @State(Scope.Benchmark) // shared between benchmark methods
    // @State(Scope.Group) // shared between benchmark group
    // @State(Scope.Thread) // per Thread
    public static class CounterState {

        volatile int value = 1;

        @Setup(Level.Iteration)
        public void setup() {
            value = 1;
        }

        @TearDown(Level.Trial)
        public void clean() {
            value = 1;
        }

    }

    @Benchmark
    public void testWithState(CounterState counterState) {
        counterState.value++;
        System.out.println(counterState.value);
    }

}
