package data.structure.sorted;

import data.structure.Collection;
import data.structure.Sorted;
import data.structure.Stack;
import data.structure.list.ArrayList;
import data.structure.stack.ArrayStack;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 二分搜索树
 *
 * @author no-today
 * @date 2018/9/27
 */
public class BSTree<E extends Comparable<E>> implements Sorted<E> {

    protected Node<E> root;
    protected int size;

    protected static class Node<E extends Comparable<E>> {
        /**
         * “节点高度”是指从该节点到它的最远叶子节点的距离 即所经过的“边”的数量
         * <p>
         * 叶子节点的高度为 0
         * 空节点的高度为 -1
         */
        int height;
        Node<E> left, right;
        E element;

        public Node(E element) {
            this.element = element;
        }

        public void refreshHeight() {
            height = Math.max(height(left), height(right)) + 1;
        }
    }

    protected static int height(Node<?> node) {
        return node == null ? -1 : node.height;
    }

    @Override
    public boolean add(E e) {
        root = insert(root, e);
        return true;
    }

    Node<E> insert(Node<E> node, E e) {
        /*
         * 1. 添加根节点, 直接返回赋值
         * 2. 添加子节点, 新增的节点要么添加在左侧要么在右侧, 当前节点的地位不会改变
         */
        if (node == null) {
            size++;
            return new Node<>(e);
        }

        if (node.element.compareTo(e) > 0) {
            node.left = insert(node.left, e);
        } else {
            node.right = insert(node.right, e);
        }

        node.refreshHeight();
        return node;
    }

    @Override
    public boolean remove(E e) {
        int eSize = size;
        root = delete(root, e);
        return size < eSize;
    }

    /*
     * 若有一方为空 用另一方顶替 (3
     *
     *       5                5
     *      / \              / \
     *     3   6    --->    1   6
     *    /                /
     *   1                0
     *  /
     * 0
     *
     * 若两边都不为空 左侧最大值/右侧最小顶替
     *
     *       5                5
     *      / \              / \
     *     3   6    --->    2   6
     *    / \              / \
     *   1   4            1   4
     *  / \              /
     * 0   2            0
     */
    Node<E> delete(Node<E> node, E e) {
        if (node == null) return null;

        if (node.element.compareTo(e) > 0) {
            node.left = delete(node.left, e);
        } else if (node.element.compareTo(e) < 0) {
            node.right = delete(node.right, e);
        } else {
            // 当待删除节点的度为0时，表示该节点是叶节点，可以直接删除。
            // 当待删除节点的度为1时，将待删除节点替换为其子节点即可。
            if (node.left == null || node.right == null) {
                size--;
                Node<E> child = node.left != null ? node.left : node.right;
                if (child == null) return null;
                else node = child;
            } else {
                // 当待删除节点的度为2时 需要用该节点的左子树最大节点 或 右子树最小节点替代
                // 也就是说 实际上删除的是 左子树最大节点 / 右子树最小节点
                E rightMin = findMinNode(node.right).element;
                node.right = delete(node.right, rightMin);
                node.element = rightMin;
            }
        }

        node.refreshHeight();
        return node;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            // 只是想覆盖测试而已
            if (Math.random() > 0.5) removeMax();
            else removeMin();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(E e) {
        return findNode(root, e) != null;
    }

    Node<E> findNode(Node<E> node, E e) {
        if (node == null) return null;

        if (node.element.compareTo(e) > 0) {
            return findNode(node.left, e);
        } else if (node.element.compareTo(e) < 0) {
            return findNode(node.right, e);
        } else {
            return node;
        }
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        Stack<Node<E>> stack = new ArrayStack<>();

        int i = 0;
        Node<E> cur = root;
        while (cur != null || !stack.isEmpty()) {
            // 将当前节点及左子树入栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }

            cur = stack.pop();
            consumer.accept(cur.element, i++);

            cur = cur.right;
        }
    }

    /**
     * 递归数据量大会爆栈
     */
    void foreach(Node<E> node, Consumer<E> consumer) {
        if (node == null) return;

        foreach(node.left, consumer);
        consumer.accept(node.element);
        foreach(node.right, consumer);
    }

    @Override
    public E[] toArray(E[] arr) {
        if (arr.length < size) {
            arr = (E[]) Array.newInstance(arr.getClass().getComponentType(), size);
        }

        E[] finalArr = arr;
        foreach((e, i) -> {
            if (i >= finalArr.length) System.out.println("out: " + e + ", arr: " + Arrays.toString(finalArr));
            finalArr[i] = e;
        });
        return finalArr;
    }

    @Override
    public E min() {
        Node<E> minNode = findMinNode(root);
        if (minNode == null) return null;
        return minNode.element;
    }

    Node<E> findMinNode(Node<E> node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public E max() {
        Node<E> maxNode = findMaxNode(root);
        if (maxNode == null) return null;
        return maxNode.element;
    }

    @Override
    public E removeMin() {
        Node<E> node = delete(root, min());
        if (node == null) return null;
        return node.element;
    }

    @Override
    public E removeMax() {
        Node<E> node = delete(root, max());
        if (node == null) return null;
        return node.element;
    }

    Node<E> findMaxNode(Node<E> node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    @Override
    public E higher(E element) {
        if (root == null) return null;
        return findGreaterThan(root, element, null);
    }

    /**
     * 从 当前节点 往下寻找比 目标节点 大的最小元素
     *
     * @param node    当前节点
     * @param element 目标节点
     * @param result  当前比目标节点大的最小节点
     */
    E findGreaterThan(Node<E> node, E element, E result) {
        /*
         * node = 10, element = 5, result = 10,7,6
         *
         *       10
         *      /  \
         *     5    15
         *    / \
         *   1   7
         *      /
         *     6
         */
        if (node == null) return result;

        // 当前节点大于目标节点
        if (node.element.compareTo(element) > 0) {
            // 记录当前大于目标节点的值, 但这还不算完, 需要继续向左子树搜索最小值
            result = node.element;
            return findGreaterThan(node.left, element, result);
        } else {
            return findGreaterThan(node.right, element, result);
        }
    }

    @Override
    public E lower(E element) {
        return findLessThan(root, element, null);
    }

    /**
     * 从 当前节点 往下寻找比 目标节点 小的最大元素
     *
     * @param node    当前节点
     * @param element 目标节点
     * @param result  当前比目标节点小的最大节点
     */
    E findLessThan(Node<E> node, E element, E result) {
        /*
         * node = 10, element = 5, result = 10,7,6
         *
         *       10
         *      /  \
         *     5    15
         *    / \
         *   1   7
         *      /
         *     6
         */
        if (node == null) return result;

        // 当前节点大于目标节点
        if (node.element.compareTo(element) >= 0) {
            return findLessThan(node.left, element, result);
        } else {
            // 记录当前小于目标节点的值, 但这还不算完, 需要继续向右子树搜索最大值
            result = node.element;
            return findLessThan(node.right, element, result);
        }
    }

    @Override
    public Collection<E> range(E start, E stop, int limit) {
        Collection<E> result = new ArrayList<>(limit);
        range(root, start, stop, limit, result);
        return result;
    }

    void range(Node<E> node, E start, E stop, int limit, Collection<E> result) {
        if (limit == -1) limit = Integer.MAX_VALUE;
        if (node == null || result.size() == limit) return;

        // 二分法探到左侧底 再向上收集
        if (node.element.compareTo(start) > 0) {
            range(node.left, start, stop, limit, result);
        }

        if (node.element.compareTo(start) >= 0 && (Objects.isNull(stop) || node.element.compareTo(stop) < 0)) {
            if (result.size() == limit) return;
            result.add(node.element);
        }

        if (Objects.isNull(stop) || node.element.compareTo(stop) < 0) {
            range(node.right, start, stop, limit, result);
        }
    }

    /**
     * 平衡因子
     */
    protected int balanceFactor(Node<?> node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * O(n) 每个节点都会参与运算
     */
    boolean isBalanced(Node<E> node) {
        // 空树是平衡的
        if (node == null) return true;

        /*
         *     10{3
         *    /  \
         *   5{2  15{0
         *  / \
         * 1{0 7{1
         *    /
         *   6{0
         */
        int balanceFactor = balanceFactor(node);
        if (Math.abs(balanceFactor) > 1) {
            return false;
        }

        return isBalanced(node.left) && isBalanced(node.right);
    }

    public int getHeight() {
        if (isEmpty()) return 0;
        return root.height;
    }
}
