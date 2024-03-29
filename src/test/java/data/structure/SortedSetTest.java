package data.structure;

import data.structure.sorted.BSTree;
import data.structure.sorted.SkipList;
import org.junit.jupiter.api.Test;

import static data.structure.utils.DST.collection;
import static data.structure.utils.DST.sortedSet;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author no-today
 * @date 2024/03/22 10:24
 */
class SortedSetTest {

    @Test
    void skipList() {
        collection(new SkipList<>());
    }

    @Test
    void bsTree() {
        BSTree<Integer> tree = new BSTree<>();
        tree.add(100);
        tree.add(50);
        tree.add(150);
        tree.add(75);
        tree.add(125);
        tree.add(25);
        tree.add(175);
        tree.add(0);
        tree.add(200);
        tree.add(200);

        assertEquals(125, tree.higher(100));
        assertEquals(75, tree.lower(100));
        assertEquals(0, tree.min());
        assertEquals(200, tree.max());

        assertArrayEquals(new Integer[]{0, 25, 50, 75, 100, 125, 150, 175, 200, 200}, tree.range(0, null, -1).toArray(new Integer[0]));
        assertArrayEquals(new Integer[]{100, 125}, tree.range(100, 200, 2).toArray(new Integer[0]));
        assertArrayEquals(new Integer[]{100}, tree.range(100, 125, 999).toArray(new Integer[0]));

        assertEquals(10, tree.size());
        tree.remove(200);
        assertEquals(9, tree.size());

        sortedSet(new BSTree<>());
    }
}