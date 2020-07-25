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
    void dequeue() {
        assertEquals(String.valueOf(0), queue.dequeue());
    }

    @Test
    void front() {
        assertEquals(String.valueOf(0), queue.front());
    }

    @Test
    void size() {
        assertEquals(SIZE, queue.size());
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