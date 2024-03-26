package data.structure;


import data.structure.stack.ArrayStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackTest {

    ArrayStack<String> stack = new ArrayStack<>();

    @Test
    public void test() throws Exception {
        stack.push("0");
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");
        stack.push("5");
        stack.push("6");
        stack.push("7");
        stack.push("8");
        stack.push("9");

        System.out.println(stack);

        assertEquals("9", stack.pop());
        assertEquals("8", stack.pop());
        assertEquals("7", stack.peek());
    }
}
