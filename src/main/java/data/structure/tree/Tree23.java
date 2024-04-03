package data.structure.tree;

import data.structure.Collection;
import data.structure.Sorted;
import data.structure.Stack;
import data.structure.stack.ArrayStack;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * 2-3 树
 * - 2 节点: 含有一个键(及其对应的值) 和两条链接(左、右), 左链接指向的节点都小于该节点, 右链接指向的节点都大于该节点
 * - 3 节点: 含有两个键(及其对应的值) 和三条链接(左、中、右), 左右同上, 中链接指向的节点都位于两个键之间
 *
 * @author no-today
 * @date 2022/05/03 13:46
 */
@Deprecated
public class Tree23<E extends Comparable<E>> implements Sorted<E> {

    private Node23<E> root;

    private int length;

    private int height;

    @Override
    public boolean add(E element) {
        if (root == null) {
            root = new Node23<>(element);
            height++;
        } else {
            // 如果返回不为空, 说明根节点进行了分裂, 把返回的中位数作为新的根节点
            Node23<E> middle = add(this.root, element);
            if (middle != null) {
                root = middle;
                height++;
            }
        }

        length++;
        return true;
    }

    /**
     * 1. 如果节点有空间容纳新元素, 将新元素插入到这一节点, 且保持节点中元素有序
     * 2. 否则这一节点已经满了, 将它分裂
     * 2.1 从该节点的原有元素和新的元素中选择出中位数
     * 2.2 小于中位数的元素放到左边节点, 大于中位数的元素放到右边节点
     * 2.3 中位数上升, 被插入到父节点中, 这可能造成父节点分裂, 分裂父节点时可能又会使它的父节点分裂,
     * 以此类推。如果没有父节点(这一节点是根节点), 就把中位数节点当作新的根节点。
     *
     * @param cur     当前游标
     * @param element 新元素
     * @return 当前节点可以容纳时返回空, 容纳不下时返回分裂后的中位数节点
     */
    private Node23<E> add(Node23<E> cur, E element) {
        // 叶子节点, 插入元素的起点
        if (cur.isLeaf()) {
            // 当前元素没有满, 把新元素加进去
            if (!cur.isFull()) {
                cur.add(element);
                return null;
            } else {
                // 当前节点已满, 分裂后把中位数节点返回给父节点
                return split(cur, new Node23<>(element));
            }
        }

        // 当前节点还不是叶子节点, 继续递归
        // 从子节点传递上来了中位数节点, 说明子节点进行了分裂
        Node23<E> middleNode = add(selectNextNode(cur, getNext(cur, element)), element);
        if (middleNode == null) return null;

        // 当前节点未满, 合并到当前节点
        if (!cur.isFull()) {
            merge(cur, middleNode);
            return null;
        } else {
            // 当前节点已满, 分裂后把中位数节点返回给父节点
            return split(cur, middleNode);
        }
    }

    /**
     * 计算出元素所在当前节点的方向
     */
    private Next getNext(Node23<E> cur, E element) {
        if (element.compareTo(cur.leftElement) < 0) {
            return Next.LEFT;
        } else if (cur.isFull() && element.compareTo(cur.rightElement) < 0) {
            return Next.MID;
        } else {
            return Next.RIGHT;
        }
    }

    /**
     * 基于方向选择节点
     */
    private Node23<E> selectNextNode(Node23<E> node, Next next) {
        switch (next) {
            case LEFT:
                return node.left;
            case MID:
                return node.mid;
            case RIGHT:
                return node.right;
            default:
                throw new IllegalStateException("Should not arrive");
        }
    }

    /**
     * 合并
     * <p>
     * 中间节点分裂会促使当前节点继续分裂, 直到根节点(不会进入该函数)
     * 如果新成员最小, 当前节点的中间节点 -> 新成员的右节点
     * 如果新成员最大, 当前节点的中间节点 -> 新成员的左节点
     *
     * @param cur       当前节点(3节点)
     * @param newMember 新成员(2节点)
     */
    private void merge(Node23<E> cur, Node23<E> newMember) {
        // 新元素和原有元素做比较, 确定新元素放的位置
        Next next = getNext(cur, newMember.leftElement);

        if (Next.LEFT.equals(next)) {
            /*
             *            2(new)                           2,4(cur)
             *          /   \                            /  |
             *         /     \                          /   |
             *        /       \                        /   P.R
             *       1(left)    3(right)    ---->     P.L
             *      / \       / \
             *     /   \     /   \
             *    L-L  L-R  R-L  L-R
             *
             * 只有左边需要维护
             * 当前节点的中节点 指向 新节点的右节点(新节点在左侧, 比当前节点元素小, 比新节点大, 所以放中间)
             * 当前节点的左节点 指向 新节点的左节点(新节点销毁了, 当前节点接管左节点)
             */
            cur.add(newMember.leftElement);
            cur.mid = newMember.right;
            cur.left = newMember.left;

            // 重设子节点指针
            resetChildParent(cur);
        } else if (Next.RIGHT.equals(next)) {
            /*
             *            4(new)                (cur)2,4
             *          /   \                         |  \
             *         /     \                        |   \
             *        /       \                      P.L   \
             *       3(left)    5(right)    ---->          P.R
             *      / \       / \
             *     /   \     /   \
             *    L-L  L-R  R-L  L-R
             *
             * 只有右边需要维护
             * 当前节点的中节点 指向 新节点的左节点(新节点在右侧, 比当前节点元素大, 比新节点小, 所以放中间)
             * 当前节点的右节点 指向 新节点的右节点(新节点销毁了, 当前节点接管右节点)
             */
            cur.add(newMember.leftElement);

            cur.mid = newMember.left;
            cur.right = newMember.right;

            // 重设子节点指针
            resetChildParent(cur);
        }
    }

    /**
     * 分裂
     * <p>
     * 维护当前节点打散后原有指针关系, 保持有序、平衡
     *
     * @param cur     当前节点(3节点)
     * @param newNode 新节点(2节点)
     * @return 分裂后的中位数节点
     */
    private Node23<E> split(Node23<E> cur, Node23<E> newNode) {
        // 新元素和原有元素做比较, 确定新元素放的位置
        Next next = getNext(cur, newNode.leftElement);

        // 新元素放到左侧
        if (Next.LEFT.equals(next)) {
            /*
             *            2(leftElement)
             *          /   \
             *         /     \
             *        /       \
             *       1(New)    3(rightElement)
             *      / \       / \
             *     /   \     /   \
             *    N-L  N-R  C-M  C-R
             */
            Node23<E> middle = new Node23<>(cur.leftElement);
            Node23<E> right = new Node23<>(cur.rightElement);

            // 从左插入, 中间节点是有值的, 中间节点要挂到右节点的左侧
            right.left = cur.mid;
            right.right = cur.right;

            // 左节点直接使用新成员
            middle.left = newNode;
            middle.right = right;

            // 重设子节点指针
            resetChildParent(right, middle);

            return middle;
        }

        // 新元素放到右侧
        if (Next.RIGHT.equals(next)) {
            /*
             *                    2(rightElement)
             *                  /   \
             *                 /     \
             *                /       \
             *  (leftElement)1         3(New)
             *              / \       / \
             *             /   \     /   \
             *            C-L  C-M  N-L  N-R
             */
            Node23<E> middle = new Node23<>(cur.rightElement);
            Node23<E> left = new Node23<>(cur.leftElement);

            // 从右边插入, 中间节点是有值的, 中间的值要挂到左节点的右侧
            left.left = cur.left;
            left.right = cur.mid;

            middle.left = left;
            middle.right = newNode;

            // 重设子节点指针
            resetChildParent(left, middle);

            return middle;
        }

        // 新元素放到中间
        if (Next.MID.equals(next)) {
            /*
             *                    2(New)
             *                  /   \
             *                 /     \
             *                /       \
             *  (leftElement)1         3(rightElement)
             *              / \       / \
             *             /   \     /   \
             *            C-L  N-L  N-R  C-R
             */
            Node23<E> left = new Node23<>(cur.leftElement);
            Node23<E> right = new Node23<>(cur.rightElement);

            // 中节点的左右元素都比左节点大, 中节点的左节点当 左节点的右节点
            left.left = cur.left;
            left.right = newNode.left;

            // 中节点的左右元素都比右节点小, 中节点的右节点当 右节点的左节点, 刚好一边分一个
            right.left = newNode.right;
            right.right = cur.right;

            newNode.left = left;
            newNode.right = right;

            // 重设子节点指针: 三个节点都有变动, 全部重置
            resetChildParent(left, right, newNode);

            return newNode;
        }

        throw new IllegalStateException();
    }

    private void resetChildParent(Node23... nodes) {
        for (Node23 node : nodes) node.relinkChildParent();
    }

    /**
     * 检查元素是否存在
     *
     * @param element target
     */
    @Override
    public boolean contains(E element) {
        if (element == null) {
            throw new NullPointerException("find target is can not null");
        }

        return findNode(root, element) != null;
    }

    /**
     * 查找元素所在节点
     *
     * @return no exists return null
     */
    private Node23<E> findNode(Node23<E> cur, E element) {
        while (cur != null) {
            // 目标在当前节点
            if (element.compareTo(cur.leftElement) == 0 || (cur.isFull() && element.compareTo(cur.rightElement) == 0)) {
                return cur;
            }

            if (element.compareTo(cur.leftElement) < 0) {
                // 目标在左侧
                cur = cur.left;
            } else if (cur.isFull() && element.compareTo(cur.rightElement) < 0) {
                // 目标比位于中间节点
                cur = cur.mid;
            } else {
                // 目标在右侧
                cur = cur.right;
            }
        }

        return null;
    }

    @Override
    public E min() {
        Node23<E> node = findMinNode(root);
        if (node == null) return null;
        return node.min();
    }

    @Override
    public E max() {
        Node23<E> node = findMaxNode(root);
        if (node == null) return null;
        return node.max();
    }

    @Override
    public E removeMin() {
        Node23<E> minNode = findMinNode(root);
        if (minNode == null) return null;
        E min = minNode.min();
        removeFromNode(minNode, min);
        return min;
    }

    @Override
    public E removeMax() {
        Node23<E> maxNode = findMaxNode(root);
        if (maxNode == null) return null;
        E max = maxNode.max();
        removeFromNode(maxNode, max);
        return max;
    }

    @Override
    public E higher(E element) {
        return null;
    }

    @Override
    public E lower(E element) {
        return null;
    }

    @Override
    public Collection<E> range(E start, E stop, int limit) {
        return null;
    }

    private Node23<E> findMinNode(Node23<E> cur) {
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    private Node23<E> findMaxNode(Node23<E> cur) {
        while (cur.right != null) cur = cur.right;
        return cur;
    }

    /**
     * 删除元素
     */
    public boolean remove(E element) {
        Node23<E> deleteNode = findNode(root, element);
        if (deleteNode == null) return false;

        removeFromNode(deleteNode, element);
        return true;
    }

    private void removeFromNode(Node23<E> node, E element) {
        // 3节点直接删
        if (node.isFull()) {
            node.del(element);
            length--;
            return;
        }

        // 根节点为2节点
        if (node.isRoot()) {
            root = null;
            length--;
            return;
        }

        /*
         * 父2, 兄3
         *
         *       4(parent)                      4
         *     /   \               ---->      /   \
         *    /     \                        /     \
         *   2(del) 6,8(brother)            3       5
         *  / \    / | \                   / \     / \
         * 1   3  5  7  9,10              dl dr
         *
         * 删除节点的值 = 父亲节点的值
         * 父亲节点的值 = 兄弟节点的小值
         */
        Node23<E> parent = node.parent;
        if (!parent.isFull() && parent.right.isFull()) {
            node.del(parent.leftElement);
        }
    }

    @Override
    public void clear() {
        while (!isEmpty()) removeMax();
    }

    public E[] toArray(E[] arr) {
        if (arr.length < length) {
            arr = (E[]) Array.newInstance(arr.getClass().getComponentType(), length);
        }

        E[] finalArray = arr;
        foreach((e, i) -> finalArray[i] = e);
        return arr;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        if (isEmpty()) return;

        Stack<Node23<E>> stack = new ArrayStack<>();

        int i = 0;
        stack.push(root);
        while (!stack.isEmpty()) {
            Node23<E> cur = stack.pop();

            if (cur.right != null) {
                stack.push(cur.right);
            }

            if (cur.isFull()) {
                consumer.accept(cur.rightElement, i++);
            }

            if (cur.mid != null) {
                stack.push(cur.mid);
            }

            if (cur.left != null) {
                stack.push(cur.left);
            }

            if (!cur.isFull()) {
                consumer.accept(cur.leftElement, i++);
            }
        }
    }

    public int height() {
        return height;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray((E[]) new Comparable[0]));
    }

    /**
     * 前序遍历(升序)
     *
     * @param cur      当前节点
     * @param consumer 通常只需要关注值即可, 某些需要 TDD 的地方需要用节点做验证
     */
    private void foreach(Node23<E> cur, BiConsumer<Node23<E>, E> consumer) {
        if (cur == null) {
            return;
        }

        foreach(cur.left, consumer);
        consumer.accept(cur, cur.leftElement);
        foreach(cur.mid, consumer);
        if (cur.isFull()) consumer.accept(cur, cur.rightElement);
        foreach(cur.right, consumer);
    }

    /**
     * 检查节点是平衡的(左右节点高度一致)
     * <p>
     * 中间节点有点特殊
     * - 为空时不参与运算
     * - 不为空则递归以下中间节点的左右高度, 再+1(自己这层)
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node23<E> node) {
        if (node == null) return true;

        if (!node.isBalanced()) {
            return false;
        }

        return isBalanced(node.left) && isBalanced(node.mid) && isBalanced(node.right);
    }

    /**
     * 校验是否是正确的父节点引用
     */
    public boolean isValidParentLink() {
        // 根节点为 2节点, 那么最多出现 1次, 3节点最多出现 2次
        AtomicInteger rootNodeCount = new AtomicInteger();
        try {
            foreach(root, (node, element) -> {
                // 叶子节点没有子节点, 不需要验证
                if (node.isLeaf()) return;
                if (node.isRoot()) {
                    rootNodeCount.incrementAndGet();
                    if (!node.isFull() && rootNodeCount.get() > 1) throw new RuntimeException("Parent is null");
                    if (node.isFull() && rootNodeCount.get() > 2) throw new RuntimeException("Parent is null");
                }
                if (!node.isValidParentLink()) throw new RuntimeException("Parent is null");
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 从哪个方向合并到当前节点的
     */
    private enum Next {
        LEFT, MID, RIGHT
    }

    private static class Node23<E extends Comparable<E>> {

        private E leftElement, rightElement;

        private Node23<E> left, mid, right;

        /**
         * 删除元素需要父节点介入
         */
        private Node23<E> parent;

        public Node23(E leftElement) {
            this.leftElement = leftElement;
        }

        /**
         * 只有未满时才能调用
         * <p>
         * 简单的维护大小
         */
        public void add(E element) {
            if (isFull()) throw new IllegalStateException("Node already full, can not add");

            if (leftElement.compareTo(element) > 0) {
                rightElement = leftElement;
                leftElement = element;
            } else {
                rightElement = element;
            }
        }

        /**
         * 只有满时才能调用
         */
        public void del(E element) {
            if (!isFull()) throw new IllegalStateException("Node not full, can not del");

            if (leftElement.compareTo(element) == 0) {
                leftElement = rightElement;
            }
            rightElement = null;
        }

        public E min() {
            return leftElement;
        }

        public E max() {
            return isFull() ? rightElement : leftElement;
        }

        public boolean isFull() {
            return rightElement != null;
        }

        /**
         * 检查是否是叶子节点
         */
        public boolean isLeaf() {
            return left == null;
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isBalanced() {
            if (left == null && mid == null && right == null) return true;
            return left != null && right != null;
        }

        /**
         * 检查当前节点子节点的父链接是否正常
         * 分裂、合并只后可能会出现当前节点孩子的父节点不是当前节点
         */
        public boolean isValidParentLink() {
            if (left != null && left.parent != this) return false;
            if (right != null && right.parent != this) return false;
            return mid == null || mid.parent == this;
        }

        /**
         * 分裂 和 合并都会使原有的父子节点关系失效
         * 需要对有改动的节点进行重新连接父子关系
         */
        public void relinkChildParent() {
            if (left != null) left.parent = this;
            if (right != null) right.parent = this;
            if (mid != null) mid.parent = this;
        }
    }
}
