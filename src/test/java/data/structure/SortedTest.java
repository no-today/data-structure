package data.structure;

import data.structure.sorted.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        sorted(new Tree23<>(), false);
    }

    @Test
    void llrbTree() {
        sorted(new LLRBTree<>(), false);
    }
}