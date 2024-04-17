package data.structure;

import data.structure.sorted.*;
import org.junit.jupiter.api.Test;

import static data.structure.utils.DST.sorted;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2024/03/22 10:24
 */
class SortedTest {

    @Test
    void skipList() {
        sorted(new SkipList<>());
    }

    @Test
    void bsTree() {
        sorted(new BSTree<>());
    }

    @Test
    void avlTree() {
        sorted(new AVLTree<>());
    }

    @Test
    void tree23() {
        int size = 15;
//        for (int i = 1; i <= size; i++) deletion(new Tree23<>(), i, false);
        deletion(new Tree23<>(), size, true);

//        sorted(new Tree23<>(), false);
    }

    @Test
    void llrbTree() {
        sorted(new LLRBTree<>(), false);
    }

    private static void deletion(Tree23<Integer> tree, int size, boolean print) {
        for (int i = 1; i <= size; i++) {
            assertTrue(tree.add(i));
            assertTrue(tree.contains(i));
            assertTrue(tree.isBalanced());
            assertEquals(i, tree.size());
        }

        for (int i = 1; i < size; i++) {
            assertEquals(i, tree.removeMin());
            assertFalse(tree.contains(i));
            assertEquals(size - i, tree.size());
            assertTrue(tree.isBalanced());

            if (print) System.out.println(tree);
        }
    }
}