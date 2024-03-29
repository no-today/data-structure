package data.structure.utils;


import data.structure.Collection;
import data.structure.Map;
import data.structure.Set;
import data.structure.SortedSet;

import java.util.List;
import java.util.Objects;
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

        System.out.printf("Total worlds: %s%n", collection.size());
        assertFalse(collection.isEmpty());

        List.of(collection.toArray(new String[0])).forEach(e -> {
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

        System.out.printf("Total worlds: %s%n", map.size());
        assertFalse(map.isEmpty());

        List.of(map.entryKey().toArray(new String[0])).forEach(e -> {
            assertNotNull(map.remove(e));
        });

        assertTrue(map.isEmpty());
    }

    public static void sortedSet(SortedSet<String> sortedSet) {
        System.out.printf("Implementation: %s\n", sortedSet.getClass().getSimpleName());

        assertTrue(sortedSet.isEmpty());
        AtomicInteger counter = new AtomicInteger(0);
        ArticleReader.read(e -> {
            if (sortedSet.add(e)) {
                counter.incrementAndGet();
            }
            assertTrue(sortedSet.contains(e));

            assertEquals(counter.get(), sortedSet.size());
        }, RESOURCES);

        System.out.printf("Total worlds: %s%n", sortedSet.size());
        assertFalse(sortedSet.isEmpty());

        String[] array = sortedSet.toArray(new String[0]);
        assertTrue(isSorted(array));

        List.of(array).forEach(e -> {
            assertTrue(sortedSet.remove(e));
            try {
                assertEquals(sortedSet.size(), counter.decrementAndGet());
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        });

        assertTrue(sortedSet.isEmpty());
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
