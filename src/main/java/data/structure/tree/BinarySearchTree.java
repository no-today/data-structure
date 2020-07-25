package data.structure.tree;

import data.structure.Stack;
import data.structure.Queue;
import data.structure.queue.LinkedQueue;
import data.structure.stack.ArrayStack;

/**
 * 二叉树
 * <p>
 * 前序
 * root => 左 => 右
 * 中序
 * 左 => root => 右
 * 后序
 * 左 => 右 => root
 *
 * @author cheng
 * @date 2018/9/27
 * @time 13:36
 */
public class BinarySearchTree<E extends Comparable<E>> {

    private Node<E> root;

    private int length;

    /**
     * 遍历转化成 Array 时使用
     */
    private int index;

    public boolean isEmpty() {
        return length == 0;
    }

    public int size() {
        return length;
    }

    /**
     * 添加元素
     */
    public void add(E e) {
        root = add(root, e);
    }

    private Node<E> add(Node<E> node, E e) {
        if (node == null) {
            length++;
            return new Node<>(e, null, null);
        }

        /*
         * 递归遍历到 null 再插入新节点
         * 	递归时会将已连接的节点重新连接一次
         */
        if (e.compareTo(node.e) < 0) {
            node.left = add(node.left, e);
        } else if (e.compareTo(node.e) > 0) {
            node.right = add(node.right, e);
        }

        /*
         * 返回的还是 root 节点
         */
        return node;
    }

    /**
     * 元素是否存在
     */
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

    /**
     * 前序遍历
     */
    public E[] preOrder() {
        if (root == null) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];
        preOrder(root, array);
        index = 0;
        return array;
    }

    private void preOrder(Node<E> node, E[] array) {
        if (node == null) {
            return;
        }

        array[index++] = node.e;
        preOrder(node.left, array);
        preOrder(node.right, array);
    }

    /**
     * 中序遍历
     */
    public E[] inOrder() {
        if (root == null) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];
        inOrder(root, array);
        index = 0;
        return array;
    }

    private void inOrder(Node<E> node, E[] array) {
        if (node == null) {
            return;
        }

        inOrder(node.left, array);
        array[index++] = node.e;
        inOrder(node.right, array);
    }

    /**
     * 后序遍历
     */
    public E[] postOrder() {
        if (root == null) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];
        postOrder(root, array);
        index = 0;
        return array;
    }

    private void postOrder(Node<E> node, E[] array) {
        if (node == null) {
            return;
        }

        postOrder(node.left, array);
        postOrder(node.right, array);
        array[index++] = node.e;
    }

    /**
     * 广度优先遍历 (BFT)
     */
    public E[] levelOrder() {
        if (root == null) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];

        Queue<Node<E>> queue = new LinkedQueue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            Node<E> node = queue.dequeue();
            array[index++] = node.e;
            if (node.left != null) {
                queue.enqueue(node.left);
            }
            if (node.right != null) {
                queue.enqueue(node.right);
            }
        }

        index = 0;
        return array;
    }

    /**
     * 最小值
     */
    public E min() {
        return min(root).e;
    }

    private Node<E> min(Node<E> node) {
        if (node.left == null) {
            return node;
        }
        return min(node.left);
    }

    /**
     * 最大值
     */
    public E max() {
        return max(root).e;
    }

    private Node<E> max(Node<E> node) {
        if (node.right == null) {
            return node;
        }
        return max(node.right);
    }

    /**
     * 删除树根
     */
    private E removeRoot() {
        E result = root.e;
        root.e = null;
        root.left = null;
        root.right = null;
        root = null;
        length--;
        return result;
    }

    /**
     * 删除最小值
     */
    public E removeMin() {
        if (root == null) {
            return null;
        }

        E min = min();
        removeMin(root);
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
        return node;
    }

    /**
     * 删除最大值
     */
    public E removeMax() {
        if (root == null) {
            return null;
        }

        E max = max();
        removeMax(root);
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
        return node;
    }

    /**
     * 删除指定节点
     */
    public boolean remove(E e) {
        return remove(root, e) != null;
    }

    private Node<E> remove(Node<E> node, E e) {
        if (node == null) {
            return null;
        }

        if (e.compareTo(node.e) < 0) {
            node.left = remove(node.left, e);
            return node;
        } else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);
            return node;
        } else { // e == node.e
            /*
             * 左节点为空：用右节点顶替
             * 右节点为空：用左节点顶替
             */
            if (node.left == null) {
                Node<E> right = node.right;
                node.right = null;
                length--;
                return right;
            }
            if (node.right == null) {
                Node<E> left = node.left;
                node.left = null;
                length--;
                return left;
            }
            /*
             * 得到被删除节点的右侧最小节点
             * 删除被删除节点的右侧最小节点
             */
            Node<E> min = min(node.right);

            /*
             * 接管被删除节点的引用
             */
            min.right = removeMin(node.right);
            min.left = node.left;

            node.left = node.right = null;

            return min;
        }
    }

    /**
     * Tree node
     *
     * @param <E>
     */
    static class Node<E extends Comparable<E>> {
        E e;
        Node<E> left;
        Node<E> right;

        Node(E e, Node<E> left, Node<E> right) {
            this.e = e;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 添加元素(非递归)
     */
    public void add0(E e) {
        Node<E> current = root;

        if (current == null) {
            root = new Node<>(e, null, null);
            return;
        }

        while (true) {
            /*
             * 相等 什么都不做
             * 大于 右边
             * 小于 左边
             */
            if (e.compareTo(current.e) == 0) {
                return;
            } else if (e.compareTo(current.e) < 0) {
                if (current.left != null) {
                    current = current.left;
                } else {
                    Node<E> newNode = new Node<>(e, null, null);
                    current.left = newNode;
                    break;
                }
            } else {
                if (current.right != null) {
                    current = current.right;
                } else {
                    Node<E> newNode = new Node<>(e, null, null);
                    current.right = newNode;
                    break;
                }
            }
        }
        length++;
    }

    public void clear() {
        while (!isEmpty()) {
            removeMax();
        }
        removeRoot();
        length = 0;
    }

    /**
     * 检查是否存在元素(非递归)
     */
    public boolean contains0(E e) {
        if (root == null) {
            return false;
        }

        Node<E> tempNode = root;

        while (tempNode != null) {
            if (e.compareTo(tempNode.e) == 0) {
                return true;
            }

            if (e.compareTo(tempNode.e) < 0) {
                tempNode = tempNode.left;
            } else {
                tempNode = tempNode.right;
            }
        }

        return false;
    }

    /**
     * 前序遍历(非递归)
     */
    public E[] preOrder0() {
        if (isEmpty()) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];
        Stack<Node<E>> stack = new ArrayStack<>();

        Node<E> current = root;
        while (current != null || !stack.isEmpty()) {
            if (current != null) {
                array[index++] = current.e;
                stack.push(current);
                current = current.left;
            } else {
                Node<E> node = stack.pop();
                current = node.right;
            }
        }
        index = 0;
        return array;
    }

    /**
     * 中虚遍历(非递归)
     */
    public E[] inOrder0() {
        if (isEmpty()) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];
        Stack<Node<E>> stack = new ArrayStack<>();

        Node<E> current = root;

        /**
         * 未找到最左侧节点 or 栈不为空
         */
        while (current != null || !stack.isEmpty()) {
            /**
             * 左侧有元素,一路压栈,直到找到最左侧节点
             */
            if (current != null) {
                stack.push(current);
                current = current.left;
            } else {
                /**
                 * 得到最左侧节点,最小值
                 */
                Node<E> node = stack.pop();
                array[index++] = node.e;
                current = node.right;
            }
        }
        index = 0;
        return array;
    }

    /**
     * 后序遍历(非递归)
     */
    public E[] postOrder0() {
        if (isEmpty()) {
            return null;
        }

        E[] array = (E[]) new Comparable[length];
        Stack<Node<E>> nodeStack = new ArrayStack<>();
        Stack<Integer> flagStack = new ArrayStack<>();

        Node<E> current = root;
        int flag = 1;
        while (current != null || !nodeStack.isEmpty()) {
            /*
             * 左节点全部入栈,并记录入栈状态
             */
            while (current != null) {
                nodeStack.push(current);
                flagStack.push(0);
                current = current.left;
            }

            /*
             * 节点栈不为空, 标记栈顶为出栈状态(没有右节点了) 消费标记
             */
            while (!nodeStack.isEmpty() && flagStack.peek() == flag) {
                flagStack.pop();
                array[index++] = nodeStack.pop().e;
            }

            /*
             * 节点栈不为空
             * 	标记栈替换成出栈状态
             * 	进入右节点
             */
            if (!nodeStack.isEmpty()) {
                flagStack.pop();
                flagStack.push(1);
                current = nodeStack.peek();
                current = current.right;
            }
        }
        index = 0;
        return array;
    }
}