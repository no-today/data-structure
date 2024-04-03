package data.structure;


import data.structure.stack.ArrayStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StackTest {

    @Test
    public void arrayStack() {
        int size = 10000;

        Stack<Integer> stack = new ArrayStack<>();
        assertTrue(stack.isEmpty());

        for (int i = 0; i < size; i++) {
            stack.push(i);
            assertEquals(i, stack.peek());
        }

        assertFalse(stack.isEmpty());
        assertEquals(size, stack.size());

        for (int i = size - 1; i >= 0; i--) {
            assertEquals(i, stack.peek());
            assertEquals(i, stack.pop());
        }

        assertTrue(stack.isEmpty());
    }
}
