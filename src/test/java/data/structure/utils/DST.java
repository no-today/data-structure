package data.structure.utils;


import data.structure.Collection;
import data.structure.Map;
import data.structure.Set;
import data.structure.Sorted;
import data.structure.sorted.AVLTree;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2024/03/26 11:06
 */
public class DST {

    static final String[] RESOURCES = new String[]{"pride-and-prejudice.txt"};

    public static void collection(Collection<String> collection) {
        System.out.printf("Implementation: %s\n", collection.getClass().getSimpleName());
        boolean skipCheckSize = collection instanceof Set;

        assertTrue(collection.isEmpty());
        AtomicInteger counter = new AtomicInteger(0);
        ArticleReader.read(e -> {
            if (collection.add(e)) {
                counter.incrementAndGet();
            }
            assertTrue(collection.contains(e));

            if (!skipCheckSize) {
                assertEquals(counter.get(), collection.size());
            }
        }, RESOURCES);

        System.out.printf("Total items: %s%n", collection.size());
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
        ArticleReader.read(e -> {
            Integer count = map.getOrDefault(e, 0) + 1;
            map.put(e, count);
        }, RESOURCES);

        System.out.printf("Total items: %s%n", map.size());
        assertFalse(map.isEmpty());

        String[] array = map.entryKey().toArray(new String[0]);
        assertEquals(array.length, map.size());

        List.of(array).forEach(e -> {
            assertNotNull(map.remove(e));
        });

        assertTrue(map.isEmpty());
    }

    public static void sorted(Sorted<String> sorted) {
        System.out.printf("Implementation: %s\n", sorted.getClass().getSimpleName());

        assertTrue(sorted.isEmpty());
        AtomicInteger counter = new AtomicInteger(0);
        ArticleReader.read(e -> {
            if (sorted.add(e)) counter.incrementAndGet();
            assertTrue(sorted.contains(e));
            assertEquals(counter.get(), sorted.size());

        }, RESOURCES);

        System.out.printf("Total items: %s%n", sorted.size());
        assertFalse(sorted.isEmpty());
        if (sorted instanceof AVLTree) assertTrue(((AVLTree<String>) sorted).isBalanced());

        String[] array = sorted.toArray(new String[0]);
        assertEquals(array.length, sorted.size());
        assertTrue(isSorted(array));

        List.of(array).forEach(e -> {
            assertTrue(sorted.contains(e));
            assertTrue(sorted.remove(e));
            assertEquals(counter.decrementAndGet(), sorted.size());
        });

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
