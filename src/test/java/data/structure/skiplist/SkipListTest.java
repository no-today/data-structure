package data.structure.skiplist;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author no-today
 * @date 2022/04/30 21:23
 */
class SkipListTest {


    @Test
    void basic() {
        int count = 1000000;
        Set<Double> exists = new HashSet<>((int) Math.ceil(count / 0.75));

        SkipList skipList = new SkipList();
        for (int i = 0; i < count; i++) {
            double score = (Math.random() * 2000000);
            exists.add(score);
            skipList.add(score);
        }
        assertEquals(count, skipList.length());

        // expected exists
        exists.forEach(e -> assertTrue(skipList.exists(e)));
    }

    @Test
    void remove() {
        SkipList skipList = new SkipList();

        skipList.add(10);
        skipList.add(15);
        skipList.add(15);
        skipList.add(20);

        assertTrue(skipList.remove(15));
        assertEquals(2, skipList.length());

        assertTrue(skipList.remove(20));
        assertEquals(1, skipList.length());

        assertTrue(skipList.remove(10));
        assertEquals(0, skipList.length());
    }

    @Test
    void range() {
        SkipList skipList = new SkipList();
        for (int i = 1; i < 1000; i++) skipList.add(i);

        assertEquals(skipList.range(10, 20, 5), Arrays.asList(10.0, 11.0, 12.0, 13.0, 14.0));
        assertEquals(skipList.range(-1, 5, 5), Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        assertEquals(skipList.range(998, 10000, -1), Arrays.asList(998.0, 999.0));
    }
}