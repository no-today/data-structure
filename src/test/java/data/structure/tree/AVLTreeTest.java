package data.structure.tree;

import data.structure.List;
import data.structure.list.ArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2022/05/07 14:23
 */
class AVLTreeTest {

    @Test
    void basic() {
        int count = 1000000;

        List<Integer> list = new ArrayList<>(count);
        AVLTree<Integer> tree = new AVLTree<>();
        TreeTestUtils.init(count, tree, list);

        // 最多搜索 H 次即可找到元素
        System.out.println("height: " + tree.height());

        assertFalse(tree.isEmpty());
        assertEquals(count, tree.size());
        assertTrue(tree.isBalanced());          // O(n)
        assertTrue(tree.isBTS());               // O(2n)

        tree.add(Integer.MIN_VALUE);
        tree.add(Integer.MAX_VALUE);

        assertEquals(Integer.MIN_VALUE, tree.removeMin());
        assertEquals(Integer.MAX_VALUE, tree.removeMax());

        Integer[] integers = list.toArray(new Integer[0]);
        assertEquals(count, integers.length);
        for (Integer integer : integers) {
            tree.remove(integer);
        }

        assertTrue(tree.isEmpty());
    }
}