package data.structure.skiplist;

import org.junit.jupiter.api.Test;

/**
 * @author no-today
 * @date 2022/04/30 22:00
 */
class SortedListTest {

    SortedList<Integer> list = new SortedList<>();

    @Test
    void add() {
        for (int i = 0; i < 10; i++) {
            list.add((int) (Math.random() * 100));
        }

        System.out.println(list);
    }
}