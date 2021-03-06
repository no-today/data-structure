package data.structure.tree;

import data.structure._tool.ReadFileWord;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.UUID;

@State(value = Scope.Benchmark)
public class BinaryTreeTest {

    BinarySearchTree<String> tree;
    ArrayList<String> words;

    @Setup
    public void init() throws Exception {
        tree = new BinarySearchTree<>();
        words = new ArrayList<>();

        ReadFileWord.read(line -> {
            for (String word : line.split(" ")) {
                tree.add(word);
                words.add(word);
            }
        }, "a-tale-of-two-cities.txt", "pride-and-prejudice.txt", "world.txt");
    }

    @Benchmark
    public void add() {
        tree.add(UUID.randomUUID().toString());
    }

    @Benchmark
    public void search() {
        tree.contains(words.get(0));
        tree.contains(words.get(words.size() / 2));
        tree.contains(words.get(words.size() - 1));
    }

    @Benchmark
    public void remove() {
        tree.remove(words.get(1));
        tree.remove(words.get(words.size() / 2 + 1));
        tree.remove(words.get(words.size() - 2));
    }

    public static void main(String[] args) throws Exception {
        String simpleName = BinaryTreeTest.class.getSimpleName();
        Options options = new OptionsBuilder()
                .include(simpleName)
                .output(simpleName + ".log")
                .forks(1)
                .build();

        new Runner(options).run();
    }
}
