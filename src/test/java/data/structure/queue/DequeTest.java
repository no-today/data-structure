package data.structure.queue;

import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2024/03/21 17:04
 */
class DequeTest {

    private Deque<String> deque;

    @BeforeEach
    void setUp() {
        deque = new Deque<>();
    }

    @Test
    void test() {
        int size = 10000;

        /*
         * 记录操作轨迹，逆向操作数据应当一致
         */
        ArrayList<Pair<Boolean, String>> records = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String e = String.valueOf(i);

            boolean operate = Math.random() < 0.5;
            records.add(new Pair<>(operate, e));

            if (operate) {
                deque.offerLast(e);
            } else {
                deque.offerFirst(e);
            }
        }

        assertFalse(deque.isEmpty());
        assertEquals(size, deque.size());

        Collections.reverse(records);
        records.forEach(e -> {
            if (e.getKey()) {
                assertEquals(e.getValue(), deque.pollLast());
            } else {
                assertEquals(e.getValue(), deque.pollFirst());
            }
        });

        assertTrue(deque.isEmpty());
    }
}