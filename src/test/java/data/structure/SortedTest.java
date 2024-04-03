package data.structure;

import data.structure.sorted.AVLTree;
import data.structure.sorted.BSTree;
import data.structure.sorted.SkipList;
import org.junit.jupiter.api.Test;

import static data.structure.utils.DST.sorted;

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
}