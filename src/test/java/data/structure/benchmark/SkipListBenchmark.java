package data.structure.benchmark;

import data.structure.skiplist.SkipList;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.UUID;

/**
 * @author no-today
 * @date 2022/05/01 23:08
 */

@Fork(1)
@State(value = Scope.Benchmark)
public class SkipListBenchmark {

    @Param({"1000000", "10000000"})
    public int size;

    @Param({"16", "32"})
    public int level;

    public double factor = 0.5;

    SkipList<String> skipList;

    public static void main(String[] args) throws Exception {
        String simpleName = SkipListBenchmark.class.getSimpleName();
        Options options = new OptionsBuilder().include(simpleName).output(simpleName + ".log").build();

        new Runner(options).run();
    }

    @Setup
    public void init() throws Exception {
        skipList = new SkipList<>(level, factor);
        for (int i = 0; i < size; i++) skipList.add(UUID.randomUUID().toString());
    }

    @Benchmark
    public void add() {
        skipList.add(UUID.randomUUID().toString());
    }

    @Benchmark
    public void remove() {
        skipList.remove(UUID.randomUUID().toString());
    }

    @Benchmark
    public void search() {
        skipList.contains(UUID.randomUUID().toString());
    }
}
