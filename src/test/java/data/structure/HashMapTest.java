package data.structure;

import data.structure.map.HashMap;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2024/03/22 10:24
 */
class HashMapTest {

    static final int SIZE = 100000;

    @Test
    void test() {
        Map<String, Integer> map = new HashMap<>(0.99f, SIZE);
        assertFalse(map.containsKey(""));

        for (int i = 1; i <= SIZE; i++) {
            String key = UUID.randomUUID().toString();
            map.put(key, i);

            assertTrue(map.containsKey(key));
            assertEquals(map.get(key), i);
            assertEquals(i, map.size());
        }

        assertEquals(SIZE, map.size());

        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    void testToString() {
        Map<String, String> map = new HashMap<>();
        map.put("no-today", "no.today@outlook.com");
        map.put("blog", "blog.cathub.me");

        System.out.println(map);
        System.out.println(map.entrySet());
        System.out.println(map.entryKey());


    }
}