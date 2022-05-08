package data.structure.benchmark;

import data.structure.tree.BinarySearchTree;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.UUID;

@Fork(1)
@State(value = Scope.Benchmark)
public class BinaryTreeBenchmark {

    @Param({"1000000", "10000000"})
    public int size;

    BinarySearchTree<String> tree;

    public static void main(String[] args) throws Exception {
        String simpleName = BinaryTreeBenchmark.class.getSimpleName();
        Options options = new OptionsBuilder()
                .include(simpleName)
                .output(simpleName + ".log")
                .build();

        new Runner(options).run();
    }

    @Setup
    public void init() throws Exception {
        tree = new BinarySearchTree<>();
        for (int i = 0; i < size; i++) tree.add(UUID.randomUUID().toString());
    }

    @Benchmark
    public void add() {
        tree.add(UUID.randomUUID().toString());
    }

    @Benchmark
    public void search() {
        tree.remove(UUID.randomUUID().toString());
    }

    @Benchmark
    public void remove() {
        tree.contains(UUID.randomUUID().toString());
    }
}
