package data.structure.tree;

import data.structure.List;
import data.structure.Tree;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author no-today
 * @date 2022/05/07 23:28
 */
public class TreeTestUtils {

    public static void init(int count, Tree<Integer> tree, List<Integer> list) {
        int i = count / 2;

        for (int element = count - 1; element >= i; element--) {
            tree.add(element);                      // O(logn)
            assertTrue(tree.contains(element));     // O(logn)

            if (list != null) list.add(element);
        }

        for (int element = 0; element < i; element++) {
            tree.add(element);                      // O(logn)
            assertTrue(tree.contains(element));     // O(logn)

            if (list != null) list.add(element);
        }
    }
}
