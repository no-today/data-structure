package data.structure.queue;

import data.structure.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    public static final int SIZE = 10000000;
    Queue<String> queue;

    @BeforeEach
    void beforeEach() {
        queue = new ArrayQueue<>();

        for (int i = 0; i < SIZE; i++) {
            queue.enqueue(String.valueOf(i));
        }
    }

    @Test
    void test() {
        for (int i = 0; i < SIZE; i++) {
            assertEquals(String.valueOf(i), queue.front());
            assertEquals(String.valueOf(i), queue.dequeue());
            assertEquals(queue.size(), SIZE - (i + 1));
        }
    }

    @Test
    void isEmpty() {
        assertFalse(queue.isEmpty());
    }

    @Test
    void clear() {
        queue.clear();
        assertTrue(queue.isEmpty());
    }
}