package data.structure.list;


import data.structure.List;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ListTest {

    @Test
    public void array() {
        test(new LinkedList<>());
    }

    @Test
    public void linked() {
        test(new ArrayList<>(3));
    }

    public void test(List<String> list) {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());

        list.add("1");

        assertFalse(list.isEmpty());

        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add(")");

        System.out.println(list);

        assertEquals(6, list.size());
        assertEquals(")", list.get(5));

        assertTrue(list.remove("1"));
        assertTrue(list.remove("3"));
        assertTrue(list.remove(")"));
        assertEquals(3, list.size());

        assertArrayEquals(new Object[]{"2", "4", "5"}, list.toArray());
    }
}
