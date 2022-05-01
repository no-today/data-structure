package data.structure.skiplist;

import data.structure.WordsReader;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * @author no-today
 * @date 2022/05/01 23:08
 */
@State(value = Scope.Benchmark)
public class SkipListBenchmarkTest {


    SkipList skipList;
    ArrayList<String> words;
    Random rand = new Random();

    @Setup
    public void init() throws Exception {
        skipList = new SkipList(32, 0.5);
        words = new ArrayList<>();

        WordsReader.read(line -> {
            for (String word : line.split(" ")) {
                skipList.add(word.hashCode());
                words.add(word);
            }
        }, "a-tale-of-two-cities.txt", "pride-and-prejudice.txt", "world.txt");
    }

    @Benchmark
    public void add() {
        skipList.add(UUID.randomUUID().toString().hashCode());
    }

    @Benchmark
    public void search() {
        skipList.exists(words.get(0).hashCode());
        skipList.exists(words.get(words.size() / 2).hashCode());
        skipList.exists(words.get(words.size() - 1).hashCode());
    }

    @Benchmark
    public void remove() {
        skipList.remove(words.get(1).hashCode());
        skipList.remove(words.get(words.size() / 2 + 1).hashCode());
        skipList.remove(words.get(words.size() - 2).hashCode());
    }

    public static void main(String[] args) throws Exception {
        String simpleName = SkipList.class.getSimpleName();
        Options options = new OptionsBuilder()
                .include(simpleName)
                .output(simpleName + ".log")
                .forks(1)
                .build();

        new Runner(options).run();
    }
}
