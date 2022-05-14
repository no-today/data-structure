package data.structure.tree;

import data.structure.Tree;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 红黑树利用颜色做逻辑层面的变换来维持平衡
 * <p>
 * 而23树则是用物理层面的节点分裂、合并来维持平衡, 会浪费更多的空间, 逻辑也更复杂.
 *
 * @author no-today
 * @date 2022/05/13 23:44
 */
public class RBTree<E extends Comparable<E>> implements Tree<E> {

    static final boolean RED = true;
    static final boolean BLACK = false;

    private Node<E> root;

    private int length;

    @Override
    public boolean contains(E element) {
        return findNode(root, element) != null;
    }

    private Node<E> findNode(Node<E> node, E element) {
        if (node == null) return null;

        if (element.compareTo(node.e) == 0) {
            return node;
        } else if (element.compareTo(node.e) < 0) {
            return findNode(node.left, element);
        } else {
            return findNode(node.right, element);
        }
    }

    public void add(E element) {
        root = add(root, element);
        root.color = BLACK;         // 最终根节点为黑色节点
    }

    private Node<E> add(Node<E> node, E element) {
        if (node == null) {
            length++;
            return new Node<>(element, RED);
        }

        if (element.compareTo(node.e) < 0) {
            node.left = add(node.left, element);
        } else {
            node.right = add(node.right, element);
        }

        return balance(node);
    }

    private Node<E> balance(Node<E> node) {
        /*
         * 右侧不允许出现红节点
         *
         * 左节点为红节点，右节点为黑节点: 左旋转
         *
         *     100                 100
         *     /  \       --->     /  \
         *  50(R) NIL            75(R) NIL
         *   /  \               /  \
         *  NIL 75(R)        50(R) NIL
         *
         * 连续两个左节点为红色: 右旋转
         *
         *        100                  75
         *       /  \                 /  \
         *      75(R) NIL   --->  50(R)  100(R)
         *     /  \               /  \
         *  50(R) NIL           NIL  NIL
         *
         * 两个子节点都为红色: 变色
         *
         *     75
         *    /  \
         *  50   100
         */
        if (isRed(node.right) && !isRed(node.left)) {
            node = leftRotate(node);
        }

        if (isRed(node.left) && isRed(node.left.left)) {
            node = rightRotate(node);
        }

        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    private void flipColors(Node<E> node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    /*
     *   node               x
     *   /  \   左旋转      / \
     * T1    x  ---->  node   T3
     *      / \        /  \
     *     T2 T3      T1  T2
     */
    private Node<E> leftRotate(Node<E> node) {
        Node<E> x = node.right;

        // 左旋
        node.right = x.left;
        x.left = node;

        x.color = node.color;
        node.color = RED;

        return x;
    }

    /**
     * node              x
     * /  \   右旋转    /  \
     * x   T1  ---->   T2  node
     * / \                  / \
     * T2 T3                T3 T1
     */
    private Node<E> rightRotate(Node<E> node) {
        Node<E> x = node.left;

        // 右旋
        node.left = x.right;
        x.right = node;

        x.color = node.color;
        node.color = RED;

        return x;
    }

    private boolean isRed(Node<E> node) {
        if (node == null) return false;
        return node.color == RED;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public String toString() {
        E[] array = (E[]) new Comparable[length];
        AtomicInteger index = new AtomicInteger();
        foreach(root, e -> array[index.getAndIncrement()] = e);
        return Arrays.toString(array);
    }

    private void foreach(Node<E> node, Consumer<E> consumer) {
        if (node == null) return;

        foreach(node.left, consumer);
        consumer.accept(node.e);
        foreach(node.right, consumer);
    }

    private static class Node<E extends Comparable<E>> {
        E e;
        Node<E> left;
        Node<E> right;
        boolean color;

        Node(E e, boolean color) {
            this.e = e;
            this.color = color;
        }
    }

}
