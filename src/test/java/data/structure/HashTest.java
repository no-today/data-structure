package data.structure;

import data.structure.hash.HashMap;
import data.structure.hash.HashSet;
import org.junit.jupiter.api.Test;

import static data.structure.utils.CollectionTests.testsCollection;
import static data.structure.utils.CollectionTests.testsMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author no-today
 * @date 2024/03/22 10:24
 */
class HashTest {

    @Test
    void hashSet() {
        testsCollection(new HashSet<>());
    }

    @Test
    void test() {
        testsMap(new HashMap<>());
    }

    @Test
    void testToString() {
        Map<String, String> map = new HashMap<>();
        map.put("no-today", "no.today@outlook.com");
        map.put("blog", "blog.cathub.me");

        assertNull(map.get("0"));
        assertEquals(map.get("no-today"), "no.today@outlook.com");
        assertEquals(map.get("blog"), "blog.cathub.me");

        System.out.println(map);
        System.out.println(map.entrySet());
        System.out.println(map.entryKey());
    }
}