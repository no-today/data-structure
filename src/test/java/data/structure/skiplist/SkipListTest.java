package data.structure.skiplist;

import data.structure.List;
import data.structure.list.ArrayList;
import data.structure.sorted.SkipList;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2022/04/30 21:23
 */
class SkipListTest {

    @Test
    void basic() {
        int count = 1000000;
        List<Integer> exists = new ArrayList<>(count);

        SkipList<Integer> skipList = new SkipList<>();
        for (int i = 0; i < count; i++) {
            int element = (int) (Math.random() * 10000000);
            skipList.add(element);
            assertTrue(skipList.contains(element));

            exists.add(element);
        }
        assertEquals(count, skipList.size());

        // expected exists
        for (int i = 0; i < exists.size(); i++) {
            assertTrue(skipList.remove(exists.get(i)));
        }

        assertEquals(0, skipList.size());
    }

    @Test
    void remove() {
        SkipList<Integer> skipList = new SkipList<>();

        skipList.add(10);
        skipList.add(15);
        skipList.add(15);
        skipList.add(20);

        assertTrue(skipList.remove(15));
        assertEquals(3, skipList.size());

        assertTrue(skipList.remove(20));
        assertEquals(2, skipList.size());

        assertTrue(skipList.remove(10));
        assertEquals(1, skipList.size());
    }

    @Test
    void range() {
        SkipList<Integer> skipList = new SkipList<>();
        for (int i = 1; i < 1000; i++) skipList.add(i);

        assertEquals(skipList.range(10, 20, 5), Arrays.asList(10, 11, 12, 13, 14));
        assertEquals(skipList.range(-1, 5, 5), Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(skipList.range(998, 10000, -1), Arrays.asList(998, 999));
    }

    @Test
    void findMax() {
        SkipList<Integer> list = new SkipList<>();
        list.add(1);
        assertFalse(list.contains(100));
        assertFalse(list.contains(-100));
    }
}