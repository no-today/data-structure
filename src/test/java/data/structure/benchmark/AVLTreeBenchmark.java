package data.structure.benchmark;

import data.structure.sorted.AVLTree;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.UUID;

@Fork(1)
@State(value = Scope.Benchmark)
public class AVLTreeBenchmark {

    @Param({"1000000", "10000000"})
    public int size;

    AVLTree<String> tree;

    public static void main(String[] args) throws Exception {
        String simpleName = AVLTreeBenchmark.class.getSimpleName();
        Options options = new OptionsBuilder()
                .include(simpleName)
                .output(simpleName + ".log")
                .build();

        new Runner(options).run();
    }

    @Setup
    public void init() throws Exception {
        tree = new AVLTree<>();
        for (int i = 0; i < size; i++) tree.add(UUID.randomUUID().toString());
    }

    @Benchmark
    public void add() {
        tree.add(UUID.randomUUID().toString());
    }

    @Benchmark
    public void remove() {
        tree.remove(UUID.randomUUID().toString());
    }

    @Benchmark
    public void search() {
        tree.contains(UUID.randomUUID().toString());
    }
}