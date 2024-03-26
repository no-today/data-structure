package data.structure.utils;


import data.structure.Collection;
import data.structure.Map;
import data.structure.Set;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2024/03/26 11:06
 */
public class CollectionTests {

    static final String[] RESOURCES = new String[]{"pride-and-prejudice.txt"};

    public static void testsCollection(Collection<String> collection) {
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
            assertEquals(collection.size(), counter.decrementAndGet());
        });

        assertTrue(collection.isEmpty());
    }

    public static void testsMap(Map<String, Integer> map) {
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
}
