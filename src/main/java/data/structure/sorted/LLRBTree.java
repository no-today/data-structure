package data.structure.sorted;

import data.structure.Collection;
import data.structure.Sorted;
import data.structure.Stack;
import data.structure.list.ArrayList;
import data.structure.stack.ArrayStack;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 左偏红黑树
 *
 * @author no-today
 * @date 2024/04/04 23:23
 */
public class LLRBTree<E extends Comparable<E>> implements Sorted<E> {

    private Node<E> root;
    private int size;
    private int height;

    public int getHeight() {
        return height;
    }

    @Override
    public boolean add(E e) {
        Node<E> newRoot = insert(root, e);
        if (root != newRoot) {
            height++;
        }
        root = newRoot;
        root.color = Node.BLACK;

        size++;
        return true;
    }

    private Node<E> insert(Node<E> node, E e) {
        if (node == null) return new Node<>(e);

        if (node.element.compareTo(e) > 0) {
            node.left = insert(node.left, e);
        } else {
            node.right = insert(node.right, e);
        }

        return balance(node);
    }

    @Override
    public boolean remove(E e) {
        return false;
    }

    private Node<E> delete(Node<E> node, Node<E> target) {
        return null;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
        height = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        if (isEmpty()) return;

        Stack<Node<E>> stack = new ArrayStack<>();
        int i = 0;

        Node<E> cur = root;
        while (cur != null || !stack.isEmpty()) {
            // 把左侧节点全部压入栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }

            // 此时 cur 已经为空了, 从左侧开始出栈
            cur = stack.pop();
            consumer.accept(cur.element, i++);

            // 按照左、中、右顺序, 把右子节点压栈, 而后又会将右子节点的所有左子节点压栈...
            cur = cur.right;
        }
    }

    @Override
    public E[] toArray(E[] arr) {
        if (arr.length < size) {
            arr = (E[]) Array.newInstance(arr.getClass().getComponentType(), size);
        }

        E[] finalArr = arr;
        foreach((e, i) -> finalArr[i] = e);
        return finalArr;
    }

    @Override
    public boolean contains(E e) {
        return findNode(root, e) != null;
    }

    private Node<E> findNode(Node<E> node, E element) {
        if (node == null) return null;

        Node<E> cur = node;
        while (cur != null) {
            if (cur.element.compareTo(element) > 0) cur = cur.left;
            else if (cur.element.compareTo(element) < 0) cur = cur.right;
            else return cur;
        }

        return null;
    }

    @Override
    public E min() {
        if (isEmpty()) return null;
        Node<E> cur = root;
        while (cur.left != null) cur = cur.left;
        return cur.element;
    }

    @Override
    public E max() {
        if (isEmpty()) return null;
        Node<E> cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.element;
    }

    @Override
    public E removeMin() {
        E min = min();
        if (min == null) return null;
        remove(min);
        return min;
    }

    @Override
    public E removeMax() {
        E max = max();
        if (max == null) return null;
        remove(max);
        return max;
    }

    @Override
    public E higher(E element) {
        return findGreaterThan(root, element);
    }

    E findGreaterThan(Node<E> node, E element) {
        if (node == null) return null;

        E result = null;
        Node<E> cur = root;
        while (cur != null) {
            if (cur.element.compareTo(element) > 0) {
                result = cur.element;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        return result;
    }

    @Override
    public E lower(E element) {
        return findLessThan(root, element);
    }

    E findLessThan(Node<E> node, E element) {
        if (node == null) return null;

        E result = null;
        Node<E> cur = node;
        while (cur != null) {
            if (cur.element.compareTo(element) >= 0) {
                cur = cur.left;
            } else {
                result = cur.element;
                cur = cur.right;
            }
        }

        return result;
    }

    @Override
    public Collection<E> range(E start, E stop, int limit) {
        if (limit == -1) limit = Integer.MAX_VALUE;

        Collection<E> result = new ArrayList<>();
        range(root, start, stop, limit, result);
        return result;
    }

    void range(Node<E> node, E start, E stop, int limit, Collection<E> result) {
        if (node == null || result.size() == limit) return;
        if (limit == -1) limit = Integer.MAX_VALUE;
        boolean nonstop = Objects.isNull(stop);

        // 如果当前节点的值大于起始值，则继续搜索左子树
        // 这将会持续压栈，直到遇到小于起始值的节点结束
        if (node.element.compareTo(start) > 0) {
            range(node.left, start, stop, limit, result);
        }

        if (result.size() == limit) return;

        // 压栈结束，开始从最接近 start 的节点出栈，升序遍历收集
        if (node.element.compareTo(start) >= 0 && (nonstop || node.element.compareTo(stop) < 0)) {
            result.add(node.element);
        }

        // 如果当前节点的值小于结束值，则继续搜索右子树
        if (nonstop || node.element.compareTo(stop) < 0) {
            range(node.right, start, stop, limit, result);
        }
    }

    private Node<E> balance(Node<E> node) {
        // 右链接为红链接时左旋
        if (isRed(node.right) && !isRed(node.left)) {
            node = leftRotate(node);
        }

        // 左侧连续两个红链接时右旋
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rightRotate(node);
        }

        // 两个子节点都为红链接, 变色(伸展运动)
        if (isRed(node.left) && isRed(node.right)) {
            node.flipColors();
        }
        return node;
    }

    private Node<E> rightRotate(Node<E> node) {
        /*
         *              10 <- unbalanced          5
         *             /  \                      / \
         *   child -> 5    ?                    0   10
         *           / \
         * added -> 0   ?
         */
        Node<E> child = node.left;

        // 需要继续维持在二者之间的节点
        node.left = child.right;
        child.right = node;

        child.color = node.color;
        node.color = Node.RED;

        return child;
    }

    private Node<E> leftRotate(Node<E> node) {
        /*
         *   0 <- unbalanced           5
         *  / \                       / \
         * ?   5 <- child            0   10
         *    / \
         *   ?   10 <- added
         */
        Node<E> child = node.right;

        // 需要继续维持在二者之间的节点
        node.right = child.left;
        child.left = node;

        child.color = node.color;
        node.color = Node.RED;

        return child;
    }

    /**
     * 将红色节点向左移动
     */
    private Node<E> moveRedLeft(Node<E> node) {
        node.flipColors();
        if (isRed(node.right.left)) {
            node.right = rightRotate(node.right);
            node = leftRotate(node);
            node.flipColors();
        }
        return node;
    }

    /**
     * 将红色节点向右移动
     */
    private Node<E> moveRedRight(Node<E> node) {
        node.flipColors();
        if (isRed(node.left.left)) {
            node = rightRotate(node);
            node.flipColors();
        }
        return node;
    }

    /**
     * 左偏红黑树的定义:
     * 0. 根节点一定是黑链接
     * 1. 只有左链接才可以是红链接
     * 2. 没有任何一个节点可以同时和两条红链接相连(left, right, parent? 只能有一条红链接)
     * 3. 红黑树是黑色完美平衡的(红色不平衡)，即任意(底层)空链接到根节点的路径上黑链接数量相同
     *
     * @param <E>
     */
    static class Node<E extends Comparable<E>> {
        static final boolean RED = true, BLACK = false;

        E element;
        boolean color;
        Node<E> left, right;

        public Node(E element) {
            this.element = element;
            this.color = RED;
        }

        public void flipColors() {
            this.color = !this.color;
            this.left.color = !this.left.color;
            this.right.color = !this.right.color;
        }
    }

    private boolean isRed(Node<?> node) {
        return node != null && node.color == Node.RED;
    }
}
