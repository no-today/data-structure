package data.structure.tree;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeTest {

    @Test
    public void test() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();

        assertEquals(0, binarySearchTree.size());
        assertTrue(binarySearchTree.isEmpty());

        binarySearchTree.add(100);
        binarySearchTree.add(90);
        binarySearchTree.add(110);
        binarySearchTree.add(80);
        binarySearchTree.add(95);
        binarySearchTree.add(105);
        binarySearchTree.add(70);
        binarySearchTree.add(85);
        binarySearchTree.add(120);
        binarySearchTree.add(115);
        binarySearchTree.add(130);

        System.out.println(binarySearchTree);

        assertEquals(130, (int) binarySearchTree.removeMax());
        assertTrue(binarySearchTree.contains(120));
        assertTrue(binarySearchTree.remove(120));
        assertFalse(binarySearchTree.contains(120));

        System.out.println("level: " + Arrays.toString(binarySearchTree.levelOrder()));
        System.out.println();
        System.out.println("pre: " + Arrays.toString(binarySearchTree.preOrder()));
        System.out.println("pre: " + Arrays.toString(binarySearchTree.preOrder0()));
        System.out.println();
        System.out.println("in: " + Arrays.toString(binarySearchTree.inOrder()));
        System.out.println("in: " + Arrays.toString(binarySearchTree.inOrder0()));
        System.out.println();
        System.out.println("post: " + Arrays.toString(binarySearchTree.postOrder()));
        System.out.println("post: " + Arrays.toString(binarySearchTree.postOrder0()));

        binarySearchTree.clear();
        assertTrue(binarySearchTree.isEmpty());
        System.out.println("clear: " + Arrays.toString(binarySearchTree.inOrder()));
    }
}
