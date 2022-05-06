package data.structure.tree;

import data.structure.List;
import data.structure.list.ArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author no-today
 * @date 2022/05/05 20:28
 */
class Tree23Test {

    @Test
    void basic() {
        int count = 10000000;

        List<Integer> list = new ArrayList<>(count);
        Tree23<Integer> tree = new Tree23<>();
        for (int i = 0; i < count; i++) {
            int element = (int) (Math.random() * 1000000000);
            tree.add(element);
            list.add(element);
        }

        assertEquals(count, tree.length());
        assertEquals(count, tree.toArray(new Integer[0]).length);

        tree.add(Integer.MIN_VALUE);
        tree.add(Integer.MAX_VALUE);

        assertEquals(Integer.MIN_VALUE, tree.min());
        assertEquals(Integer.MAX_VALUE, tree.max());

        // 最多搜索 H 次即可找到元素
        System.out.println("height: " + tree.balanceHeight());
    }

    @Test
    void add() {
        int count = 10000;

        Tree23<Integer> tree = new Tree23<>();
        for (int i = 1; i < count; i++) {
            tree.add((int) (Math.random() * 1000000));
//            tree.add(i);
            assertTrue(tree.correctParent());
        }
    }
}