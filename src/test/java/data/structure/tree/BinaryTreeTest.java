package data.structure.tree;

import data.structure.List;
import data.structure.list.ArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2022/05/07 14:23
 */
class BinaryTreeTest {

    @Test
    void basic() {
        // 不平衡, 会递归查找高度次, 导致 StackOverflowError
        // 平衡二叉树, 即使 100w 数据也只需要查找 20 次
        int count = 1000;

        List<Integer> list = new ArrayList<>(count);
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        TreeTestUtils.init(count, tree, list);

        assertEquals(count, tree.size());
        assertFalse(tree.isEmpty());
        assertTrue(tree.isBTS());                   // O(2n)

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