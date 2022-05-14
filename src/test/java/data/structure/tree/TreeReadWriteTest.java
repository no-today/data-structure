package data.structure.tree;

import data.structure.Tree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 测试读写各个树实现的耗时
 *
 * @author no-today
 * @date 2022/05/14 12:04
 */
public class TreeReadWriteTest {

    static int count = 20000000;

    static boolean isWriteSequentially = true;

    static Random random = new Random();

    static List<Integer> datas;

    @BeforeAll
    static void initData() {
        datas = new ArrayList<>(count);
        if (isWriteSequentially) for (int i = 0; i < count; i++) datas.add(i);
        else for (int i = 0; i < count; i++) datas.add(random.nextInt(Integer.MAX_VALUE));
    }

    void test(Tree<Integer> tree) {
        long start = System.nanoTime();
        for (Integer data : datas) tree.add(data);
        System.out.printf("%s: %f s\n", tree.getClass().getSimpleName(), (System.nanoTime() - start) / 1000000000.0);
    }

    @Test
    void binary() {
        // 顺序写直接爆栈
        if (isWriteSequentially) return;
        test(new BinarySearchTree<>());
    }

    @Test
    void avl() {
        test(new AVLTree<>());
    }

    @Test
    void tree23() {
        test(new Tree23<>());
    }

    @Test
    void rb() {
        test(new RBTree<>());
    }
}

/*
 * 随机写 1000W
 *
 * BinarySearchTree: 13.534075 s
 * Tree23: 14.352599 s
 * RBTree: 16.797313 s
 * AVLTree: 16.779499 s
 *
 * 随机写 2000W
 *
 * BinarySearchTree: 33.829636 s
 * Tree23: 37.612225 s
 * RBTree: 41.739429 s
 * AVLTree: 41.262980 s
 *
 * 顺序写 1000W
 *
 * Tree23: 1.710686 s
 * RBTree: 1.953654 s
 * AVLTree: 1.389659 s
 *
 * 顺序写 2000W
 *
 * Tree23: 3.398842 s
 * RBTree: 4.578844 s
 * AVLTree: 4.127954 s
 */