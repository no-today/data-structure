package data.structure;

import data.structure.list.ArrayList;
import data.structure.queue.ArrayQueue;
import data.structure.queue.LinkedDeque;
import data.structure.queue.LinkedQueue;
import data.structure.queue.PriorityQueue;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.Test;

import static data.structure.utils.DST.collection;
import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    @Test
    void arrayQueue() {
        collection(new ArrayQueue<>());
    }

    @Test
    void linkedQueue() {
        collection(new LinkedQueue<>());
    }

    @Test
    void priorityQueue() {
        collection(new PriorityQueue<>());
    }

    @Test
    void linkedDeque() {
        collection(new LinkedDeque<>());
    }

    @Test
    void linkedDeque1() {
        int size = 10000;
        LinkedDeque<String> linkedDeque = new LinkedDeque<>();

        // 记录操作轨迹，逆向操作数据应当一致
        List<Pair<Boolean, String>> records = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String e = String.valueOf(i);

            boolean operate = Math.random() < 0.5;
            records.add(new Pair<>(operate, e));

            if (operate) {
                linkedDeque.offerLast(e);
            } else {
                linkedDeque.offerFirst(e);
            }
        }

        assertFalse(linkedDeque.isEmpty());
        assertEquals(size, linkedDeque.size());

        for (int i = records.size() - 1; i >= 0; i--) {
            Pair<Boolean, String> pair = records.get(i);
            if (pair.getKey()) {
                assertEquals(pair.getValue(), linkedDeque.pollLast());
            } else {
                assertEquals(pair.getValue(), linkedDeque.pollFirst());
            }
        }

        assertTrue(linkedDeque.isEmpty());
    }
}