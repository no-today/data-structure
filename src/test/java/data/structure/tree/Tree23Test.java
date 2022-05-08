package data.structure.tree;

import data.structure.List;
import data.structure.list.ArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author no-today
 * @date 2022/05/05 20:28
 */
class Tree23Test {

    @Test
    void basic() {
        int count = 1000000;

        Tree23<Integer> tree = new Tree23<>();
        for (int i = 0; i < count; i++) {
            int element = (int) (Math.random() * 10000000);
            tree.add(element);                      // O(logn)
            assertTrue(tree.contains(element));     // O(logn)
        }

        assertTrue(tree.isBalanced());          // O(n)
        assertTrue(tree.isValidParentLink());   // O(n)

        assertEquals(count, tree.size());
        assertEquals(count, tree.toArray(new Integer[0]).length);

        tree.add(Integer.MIN_VALUE);
        tree.add(Integer.MAX_VALUE);

        assertEquals(Integer.MIN_VALUE, tree.min());
        assertEquals(Integer.MAX_VALUE, tree.max());

        // 最多搜索 H 次即可找到元素
        System.out.println("height: " + tree.height());
    }

    /**
     * 删除根节点
     */
    @Test
    void remove0() {
        // 删除算法太复杂了,
        // 面向 TDD 慢慢磨, 最后再优化
        Tree23<Integer> tree = new Tree23<>();
        tree.add(1);
        tree.add(2);

        assertTrue(tree.remove(1));
        assertTrue(tree.isBalanced());
        assertTrue(tree.isValidParentLink());

        assertEquals(1, tree.size());

        assertTrue(tree.remove(2));
        assertTrue(tree.isBalanced());
        assertTrue(tree.isValidParentLink());
        assertTrue(tree.isEmpty());
    }

    /**
     * 从3节点
     */
    @Test
    void remove1() {

    }

    /**
     * 计算高度为 H 的23树可以容纳多少元素
     */
    @Test
    void nodes() {
        System.out.println(getNodes(25, 2));
    }

    /**
     * 根据高度和指针树得到节点数
     *
     * @param h 高度
     * @param p 节点数
     * @return 节点数
     */
    private long getNodes(int h, int p) {
        System.out.printf("Level: %s, nodes: %s%n", 1, 1);
        for (int i = 1; i < h; i++) {
            System.out.printf("Level: %s, nodes: %s%n", i + 1, (long) Math.ceil(Math.pow(p, i)));
        }
        System.out.println("------------------------------------------");
        return (long) Math.ceil(Math.pow(p, h - 1));
    }
}

/*
 * 随机 100W:
 *  AVLTree 24
 *  23Tree  16
 *
 * 随机 1000W:
 *  AVLTree 27
 *  23Tree  19
 *
 * 23Tree 相比 二叉树 缩减了 30% 的层级
 *
 * ----------------------------------------
 * 二叉树高度与节点数对照(满载情况) 2的H次方
 * Level: 1, nodes: 1
 * Level: 2, nodes: 2
 * Level: 3, nodes: 4
 * Level: 4, nodes: 8
 * Level: 5, nodes: 16
 * Level: 6, nodes: 32
 * Level: 7, nodes: 64
 * Level: 8, nodes: 128
 * Level: 9, nodes: 256
 * Level: 10, nodes: 512
 * Level: 11, nodes: 1024
 * Level: 12, nodes: 2048
 * Level: 13, nodes: 4096
 * Level: 14, nodes: 8192
 * Level: 15, nodes: 16384
 * Level: 16, nodes: 32768
 * Level: 17, nodes: 65536
 * Level: 18, nodes: 131072
 * Level: 19, nodes: 262144
 * Level: 20, nodes: 524288
 * Level: 21, nodes: 1048576
 * Level: 22, nodes: 2097152
 * Level: 23, nodes: 4194304
 * Level: 24, nodes: 8388608
 * Level: 25, nodes: 16777216
 */