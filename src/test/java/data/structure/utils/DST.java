package data.structure.utils;


import data.structure.Collection;
import data.structure.Map;
import data.structure.Set;
import data.structure.Sorted;
import data.structure.hash.HashMap;
import data.structure.list.ArrayList;
import data.structure.sorted.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2024/03/26 11:06
 */
public class DST {

    private static final int LIMIT = 10000;

    public static void collection(Collection<String> collection) {
        System.out.printf("Implementation: %s\n", collection.getClass().getSimpleName());
        boolean skipCheckSize = collection instanceof Set;

        assertTrue(collection.isEmpty());
        AtomicInteger counter = new AtomicInteger(0);
        ArticleReader.read(LIMIT, e -> {
            if (collection.add(e)) {
                counter.incrementAndGet();
            }
            assertTrue(collection.contains(e));

            if (!skipCheckSize) {
                assertEquals(counter.get(), collection.size());
            }
        });

        if (collection instanceof ArrayList) {
            System.out.printf("Total items: %s, Resizes: %s\n", collection.size(), ((ArrayList<String>) collection).getResizeCount());
        } else {
            System.out.printf("Total items: %s%n", collection.size());
        }
        assertFalse(collection.isEmpty());

        String[] array = collection.toArray(new String[0]);
        assertEquals(array.length, collection.size());

        List.of(array).forEach(e -> {
            assertTrue(collection.remove(e));
            try {
                assertEquals(collection.size(), counter.decrementAndGet());
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        });

        assertTrue(collection.isEmpty());
    }

    public static void map(Map<String, Integer> map) {
        System.out.printf("Implementation: %s\n", map.getClass().getSimpleName());

        assertTrue(map.isEmpty());
        ArticleReader.read(Integer.MAX_VALUE, e -> {
            Integer count = map.getOrDefault(e, 0) + 1;
            map.put(e, count);
        });

        if (map instanceof HashMap) {
            System.out.printf("Total items: %s, Resizes: %s\n", map.size(), ((HashMap<String, Integer>) map).getResizeCount());
        } else {
            System.out.printf("Total items: %s%n", map.size());
        }
        assertFalse(map.isEmpty());

        String[] array = map.entryKey().toArray(new String[0]);
        assertEquals(array.length, map.size());

        List.of(array).forEach(e -> assertNotNull(map.remove(e)));

        assertTrue(map.isEmpty());
    }

    public static void sorted(Sorted<Integer> sorted) {
        sorted(sorted, true);
    }

    public static void sorted(Sorted<Integer> sorted, boolean deleted) {
        System.out.printf("Implementation: %s\n", sorted.getClass().getSimpleName());

        assertTrue(sorted.isEmpty());
        AtomicInteger counter = new AtomicInteger(0);
        ArticleReader.read(Integer.MAX_VALUE, e -> {
            int val = e.hashCode();
            if (sorted.add(val)) counter.incrementAndGet();
            assertTrue(sorted.contains(val));
            assertEquals(counter.get(), sorted.size());
        });

        if (sorted instanceof BSTree) {
            System.out.printf("Total items: %s, height: %s%n", sorted.size(), ((BSTree<Integer>) sorted).getHeight());
        } else if (sorted instanceof Tree23) {
            System.out.printf("Total items: %s, height: %s%n", sorted.size(), ((Tree23<Integer>) sorted).getHeight());
        } else if (sorted instanceof LLRBTree) {
            System.out.printf("Total items: %s, levels: %s%n", sorted.size(), ((LLRBTree<Integer>) sorted).getHeight());
        } else if (sorted instanceof SkipList) {
            System.out.printf("Total items: %s, levels: %s%n", sorted.size(), ((SkipList<Integer>) sorted).getLevel());
        } else {
            System.out.printf("Total items: %s%n", sorted.size());
        }

        assertFalse(sorted.isEmpty());
        assertBalanced(sorted);

        Integer[] array = sorted.toArray(new Integer[0]);
        assertEquals(array.length, sorted.size());
        assertTrue(isSorted(array));

        if (deleted) {
            List.of(array).forEach(e -> {
                assertTrue(sorted.contains(e));
                assertTrue(sorted.remove(e));
                assertEquals(counter.decrementAndGet(), sorted.size());
            });
        } else {
            sorted.clear();
        }

        assertTrue(sorted.isEmpty());
        sortedFeatures(sorted, deleted);
    }

    private static void assertBalanced(Sorted<Integer> sorted) {
        if (sorted instanceof AVLTree) assertTrue(((AVLTree<Integer>) sorted).isBalanced());
        if (sorted instanceof Tree23) assertTrue(((Tree23<Integer>) sorted).isBalanced());
    }

    public static void sortedFeatures(Sorted<Integer> sorted, boolean deleted) {
        sorted.add(100);
        sorted.add(50);
        sorted.add(150);
        sorted.add(75);
        sorted.add(125);
        sorted.add(25);
        sorted.add(175);
        sorted.add(0);
        sorted.add(200);
        sorted.add(200);

        assertEquals(125, sorted.higher(100));
        assertEquals(75, sorted.lower(100));
        assertEquals(0, sorted.min());
        assertEquals(200, sorted.max());

        assertArrayEquals(new Integer[]{0, 25, 50, 75, 100, 125, 150, 175, 200, 200}, sorted.range(0, null, -1).toArray(new Integer[0]));
        assertArrayEquals(new Integer[]{100, 125}, sorted.range(100, 200, 2).toArray(new Integer[0]));
        assertArrayEquals(new Integer[]{100}, sorted.range(100, 125, 999).toArray(new Integer[0]));

        assertEquals(10, sorted.size());
        if (deleted) {
            assertEquals(0, sorted.removeMin());
            assertEquals(9, sorted.size());
        }

        sorted.clear();
        assertTrue(sorted.isEmpty());

        sortedCompare(sorted, deleted);
    }

    public static void sortedCompare(Sorted<Integer> sorted, boolean deleted) {
        for (int i = 1; i <= 10000; i++) {
            sorted.add(i);
            assertEquals(1, sorted.min());
            assertEquals(i, sorted.max());
            assertNull(sorted.higher(i));
            if (i > 1) assertEquals(i - 1, sorted.lower(i));
        }

        if (deleted) {
            for (int i = 1; i <= 10000; i++) {
                assertEquals(i, sorted.removeMin());
                assertBalanced(sorted);
            }
        } else {
            sorted.clear();
        }

        assertTrue(sorted.isEmpty());
    }

    private static <E extends Comparable<E>> boolean isSorted(E[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }
}
