package data.structure.tree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author no-today
 * @date 2022/05/14 10:22
 */
class RBTreeTest {

    @Test
    void add() {
        int count = 10000;

        RBTree<Integer> tree = new RBTree<>();
        for (int i = 0; i < count; i++) {
            tree.add((int) (Math.random() * (count * 10)));
        }

        System.out.println(tree);
    }
}