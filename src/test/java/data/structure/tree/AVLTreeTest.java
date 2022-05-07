package data.structure.tree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author no-today
 * @date 2022/05/07 14:23
 */
class AVLTreeTest {

    @Test
    void basic() {
        int count = 1000000;

        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 0; i < count; i++) {
            int element = (int) (Math.random() * 10000000);
            tree.add(element);                      // O(logn)
            assertTrue(tree.contains(element));     // O(logn)
        }

        assertTrue(tree.isBalanced());          // O(n)
        assertTrue(tree.isBTS());               // O(2n)

        // 最多搜索 H 次即可找到元素
        System.out.println("height: " + tree.height());
    }
}