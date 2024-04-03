package data.structure.utils;


import data.structure.Collection;
import data.structure.Map;
import data.structure.Set;
import data.structure.Sorted;
import data.structure.hash.HashMap;
import data.structure.list.ArrayList;
import data.structure.sorted.AVLTree;
import data.structure.sorted.BSTree;
import data.structure.sorted.SkipList;

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
        } else if (sorted instanceof SkipList) {
            System.out.printf("Total items: %s, levels: %s%n", sorted.size(), ((SkipList<Integer>) sorted).getLevel());
        } else {
            System.out.printf("Total items: %s%n", sorted.size());
        }
        assertFalse(sorted.isEmpty());
        if (sorted instanceof AVLTree) assertTrue(((AVLTree<Integer>) sorted).isBalanced());

        Integer[] array = sorted.toArray(new Integer[0]);
        assertEquals(array.length, sorted.size());
        assertTrue(isSorted(array));

        List.of(array).forEach(e -> {
            assertTrue(sorted.contains(e));
            assertTrue(sorted.remove(e));
            assertEquals(counter.decrementAndGet(), sorted.size());
        });

        assertTrue(sorted.isEmpty());
        sortedBase(sorted);
    }

    private static void sortedBase(Sorted<Integer> sorted) {
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
        sorted.removeMax();
        assertEquals(9, sorted.size());

        sorted.clear();
        assertTrue(sorted.isEmpty());

        for (int i = 0; i < 10000; i++) {
            assertTrue(sorted.add(i));
            assertTrue(sorted.contains(i));
            assertEquals(i + 1, sorted.size());
        }

        for (int i = 0; i < 10000; i++) assertTrue(sorted.remove(i));
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
