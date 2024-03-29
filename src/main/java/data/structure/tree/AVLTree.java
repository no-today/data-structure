package data.structure.tree;

import data.structure.Tree;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在计算机科学中，AVL树是最早被发明的自平衡二叉查找树。在AVL树中，任一节点对应的两棵子树的最大高度差为1，因此它也被称为高度平衡树。
 * 查找、插入和删除在平均和最坏情况下的时间复杂度都是 O(logn)。增加和删除元素的操作则可能需要借由一次或多次树旋转，以实现树的重新平衡。
 * AVL树得名于它的发明者G. M. Adelson-Velsky和Evgenii Landis，他们在1962年的论文《An algorithm for the organization of information》中公开了这一数据结构。
 * <p>
 * 节点的平衡因子是它的左子树的高度减去它的右子树的高度（有时相反）。带有平衡因子1、0或 -1的节点被认为是平衡的。带有平衡因子 -2或2的节点被认为是不平衡的，
 * 并需要重新平衡这个树。平衡因子可以直接存储在每个节点中，或从可能存储在节点中的子树高度计算出来。
 * <p>
 * 平衡二叉树
 * <p>
 * 对于任意一个节点，左子树和右子数的高度差不超过 1
 * <p>
 */
public class AVLTree<E extends Comparable<E>> implements Tree<E> {

    private Node<E> root;

    private int length;

    public int height() {
        return height(root);
    }

    private int height(Node<E> node) {
        return node == null ? 0 : node.height;
    }

    private int getBalanceFactor(Node<E> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private int updateHeight(Node<E> node) {
        return node == null ? 0 : Math.max(height(node.left), height(node.right)) + 1;
    }

    /**
     * Tree node
     *
     * @param <E>
     */
    static class Node<E extends Comparable<E>> {
        E e;
        Node<E> left, right;
        int height;

        Node(E e, Node<E> left, Node<E> right) {
            this(e, left, right, 1);
        }

        Node(E e, Node<E> left, Node<E> right, int height) {
            this.e = e;
            this.left = left;
            this.right = right;
            this.height = height;
        }
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public void add(E e) {
        root = add(root, e);
    }

    private Node<E> add(Node<E> node, E e) {
        if (node == null) {
            length++;
            return new Node<>(e, null, null);
        }

        /*
         * 检测到 null 才插入新节点
         *  递归时会将已连接到节点重新连接一次
         */
        if (e.compareTo(node.e) < 0) {
            node.left = add(node.left, e);
        } else if (e.compareTo(node.e) > 0) {
            node.right = add(node.right, e);
        } else {
            node.e = e;
        }

        return balance(node);
    }

    /**
     * 平衡输入节点
     *
     * @param node node
     * @return 平衡后的节点
     */
    private Node<E> balance(Node<E> node) {
        // 刷新当前节点的高度
        node.height = updateHeight(node);

        int diff = getBalanceFactor(node);

        // LL, 左边比右边大, 并且左子节点的左边必定比右边大, 可能双边都不为空
        if (diff > 1 && getBalanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }

        // RR
        if (diff < -1 && getBalanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }

        // LR
        if (diff > 1 && getBalanceFactor(node.left) < 0) {
            // 先左旋, 变成LL再右旋
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (diff < -1 && getBalanceFactor(node.right) > 0) {
            // 先右旋, 变成RR再左旋
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        /*
         * 返回自己，会重新建立连接
         */
        return node;
    }

    /**
     * left = node.left
     * left.right = node
     * node.left = left.right
     *
     *        y                      x
     *       / \                   /   \
     *      x   T4                z     y
     *     / \        ------>    / \   / \
     *    z  T3                 T1 T2 T3 T4
     *   / \
     *  T1  T2
     */
    public Node<E> rightRotate(Node<E> node) {
        Node<E> left = node.left;

        // 右旋
        node.left = left.right;
        left.right = node;

        // 右旋时: 当前节点 & 当前节点的左节点高度会变
        node.height = updateHeight(node);
        left.height = updateHeight(left);

        return left;
    }

    /**
     * 自己测试时用的
     */
    public void rightRotate() {
        root = rightRotate(root);
    }

    /**
     * 自己测试时用的
     */
    public void leftRotate() {
        root = leftRotate(root);
    }

    private Node<E> leftRotate(Node<E> node) {
        Node<E> right = node.right;

        // 左旋
        node.right = right.left;
        right.left = node;

        // 左旋时: 当前节点 & 当前节点的右节点高度会变
        node.height = updateHeight(node);
        right.height = updateHeight(right);

        return right;
    }

    public E min() {
        if (isEmpty()) {
            return null;
        }
        return min(root).e;
    }

    private Node<E> min(Node<E> node) {
        if (node.left == null) {
            return node;
        }

        return min(node.left);
    }

    public E max() {
        if (isEmpty()) {
            return null;
        }
        return max(root).e;
    }

    private Node<E> max(Node<E> node) {
        if (node.right == null) {
            return node;
        }

        return max(node.right);
    }

    public E removeMin() {
        if (isEmpty()) {
            return null;
        }

        E min = min();
        root = removeMin(root);

        return min;
    }

    private Node<E> removeMin(Node<E> node) {
        if (node.left == null) {
            Node<E> right = node.right;
            node.right = null;
            length--;

            return right;
        }

        node.left = removeMin(node.left);
        return balance(node);
    }

    public E removeMax() {
        if (isEmpty()) {
            return null;
        }

        E max = max();
        root = removeMax(root);
        return max;
    }

    private Node<E> removeMax(Node<E> node) {
        if (node.right == null) {
            Node<E> left = node.left;
            node.left = null;
            length--;
            return left;
        }

        node.right = removeMax(node.right);

        return balance(node);
    }

    public void remove(E e) {
        root = remove(root, e);
    }

    private Node<E> remove(Node<E> node, E e) {
        if (node == null) {
            return null;
        }

        Node<E> retNode;
        if (e.compareTo(node.e) < 0) {
            node.left = remove(node.left,e);

            retNode = node;
        } else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);

            retNode = node;
        } else {  // e.compareTo(node.e) == 0
            length--;

            /*
             * 一边为空则用另一边代替
             * 都不为空时使用右侧最小元素替代
             */
            if (node.left == null) {
                Node<E> right = node.right;
                node.right = null;

                retNode = right;
            } else if (node.right == null) {
                Node<E> left = node.left;
                node.left = null;

                retNode = left;
            } else {
                // 临时存储后删除右侧最小元素
                Node<E> min = min(node.right);

                // 最小节点移动到当前节点位置，接替左右指向
                min.right = removeMin(node.right);
                min.left = node.left;

                // 清除指向
                node.left = node.right = null;

                retNode = min;
            }
        }

        return retNode == null ? null : balance(retNode);
    }

    /**
     * 中序遍历: 左 -> 中 -> 右
     * @return array
     */
    public E[] inOrder() {
        if (isEmpty()) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];
        inOrder(root, array, new AtomicInteger());
        return array;
    }

    private void inOrder(Node<E> node, E[] array, AtomicInteger index) {
        if (node == null) {
            return;
        }

        inOrder(node.left, array, index);
        array[index.getAndIncrement()] = node.e;
        inOrder(node.right, array, index);
    }

    public boolean isBTS() {
        if (root == null) {
            return true;
        }

        E[] array = inOrder();
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node<E> node) {
        if (node == null) {
            return true;
        }

        int balanceFactor = getBalanceFactor(node);
        if (Math.abs(balanceFactor) > 1) {
            return false;
        }

        return isBalanced(node.left) && isBalanced(node.right);
    }

    /**
     * 元素是否存在
     */
    @Override
    public boolean contains(E e) {
        return contains(root, e);
    }

    private boolean contains(Node<E> node, E e) {
        if (node == null) {
            return false;
        }

        /*
         * 匹配
         */
        if (e.compareTo(node.e) == 0) {
            return true;
        }

        /*
         * 往左/右找
         */
        if (e.compareTo(node.e) < 0) {
            return contains(node.left, e);
        } else if (e.compareTo(node.e) > 0) {
            return contains(node.right, e);
        }

        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(inOrder());
    }
}
