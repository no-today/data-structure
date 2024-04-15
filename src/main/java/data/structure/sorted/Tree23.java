package data.structure.sorted;

import data.structure.Collection;
import data.structure.Set;
import data.structure.Sorted;
import data.structure.Stack;
import data.structure.hash.HashSet;
import data.structure.list.ArrayList;
import data.structure.stack.ArrayStack;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 2-3树在标准二叉树的基础上引入了3节点。
 * <p>
 * N节点的意思是持有N条链接, 标准二叉树有 left、right, 相当于2树, 而 2-3 树的节点可能有2条也可能有3条链接。
 * <p>
 * 2-3树添加元素只会挂在叶子节点上, 而不是像普通二叉树在底部增高一层
 * 底部叶子节点满载时会分裂而后中位数上浮, 上浮的中位数融合到父节点. 父节点也可能满载
 * 如果所有的父节点都满载, 最终则会触发根节点分裂, 中位数成为新的根节点
 * <p>
 * 只有全部满载触发根节点分裂(伸展运动)时才会使树层高加一, 通过这个机制控制整棵树的绝对平衡
 *
 * @author no-today
 * @date 2024/04/03 16:17
 */
public class Tree23<E extends Comparable<E>> implements Sorted<E> {

    private Node<E> root;
    private int size;
    private int height;

    @Override
    public boolean add(E e) {
        Node<E> middle = insert(root, e, true);
        if (middle != null) {
            root = middle;

            // 根节点产生了分裂, 变高一层
            height++;
        }
        size++;
        return true;
    }


    /**
     * 1. 添加到二节点
     * 2. 添加到三节点
     * 2.1 三节点的父节点是二节点
     * 2.2 三节点的父节点是三节点
     *
     * @param node 当前节点
     * @param e    新元素
     * @return 如果添加到了二节点则返回 null, 如果添加到3节点, 则返回3节点分裂后的中位数节点
     */
    Node<E> insert(Node<E> node, E e, boolean merge) {
        if (node == null) return new Node<>(e);

        // 2-3树都是添加在叶子节点上, 满了之后向上生长(分裂)
        if (node.isLeaf()) {
            if (!node.is3Node()) {
                merge(node, new Node<>(e));
                return null;
            } else {
                // 当前节点已满, 分裂后把中位数返回给父节点
                return split(node, new Node<>(e));
            }
        }

        Node<E> nextNode;
        if (e.compareTo(node.leftElement) < 0) nextNode = node.left;
        else if (node.is3Node() && e.compareTo(node.rightElement) < 0) nextNode = node.mid;
        else nextNode = node.right;

        // 子节点的中间位节点, 如果为空说明子节点未发生分裂, 父节点链接关系不受影响
        // 如果不为空, 需要将分裂出来的节点融合到当前节点
        Node<E> childMiddle = insert(nextNode, e, merge);
        if (childMiddle == null) return null;
        else if (!merge) return childMiddle;

        if (!node.is3Node()) {
            // 向上融合过程中遇到一个二节点即可停止
            merge(node, childMiddle);
            return null;
        } else {
            // 若当前节点也已满, 继续向上分裂, 可能会一直分裂到 root 节点, 这时会产生新的 root
            return split(node, childMiddle);
        }
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
    public boolean contains(E e) {
        return findNode(root, e) != null;
    }

    Node<E> findNode(Node<E> node, E e) {
        if (node == null) return null;

        if (e.compareTo(node.leftElement) == 0 || (node.is3Node() && e.compareTo(node.rightElement) == 0)) {
            return node;
        }

        if (e.compareTo(node.leftElement) < 0) {
            return findNode(node.left, e);
        } else if (node.is3Node() && e.compareTo(node.rightElement) < 0) {
            return findNode(node.mid, e);
        } else {
            return findNode(node.right, e);
        }
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        Stack<Node<E>> stack = new ArrayStack<>();
        Set<Node<E>> visited = new HashSet<>();

        int i = 0;
        Node<E> cur = root;
        while (cur != null || !stack.isEmpty()) {
            // 从当前节点一路压栈到最左侧节点
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }

            // 从最左侧节点开始出栈, 逐个按左 中 右顺序消费
            cur = stack.pop();

            // 每个存在中间节点的元素都会消费两次: left,leftElement AND rightElement,right
            // 第一次处理全部左侧节点, 以及左元素
            // 第二次消费右元素, 以及处理右子节点
            boolean isFirst = visited.add(cur);
            if (isFirst) {
                consumer.accept(cur.leftElement, i++);
            } else {
                consumer.accept(cur.rightElement, i++);
            }

            // 如果存在中间节点，等到消费完全部中间节点之后，需要再消费一次当前节点的右元素和右节点
            if (isFirst && cur.is3Node()) {
                stack.push(cur);
                cur = cur.mid;
            } else {
                cur = cur.right;
            }
        }
    }

    // 递归 O(n) 会爆栈
    private void foreach(Node<E> cur, BiConsumer<Node<E>, E> consumer) {
        if (cur == null) return;

        foreach(cur.left, consumer);
        consumer.accept(cur, cur.leftElement);
        foreach(cur.mid, consumer);
        if (cur.is3Node()) consumer.accept(cur, cur.rightElement);
        foreach(cur.right, consumer);
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
    public E min() {
        Node<E> minNode = findMinNode(root);
        if (minNode == null) return null;
        return minNode.leftElement;
    }

    Node<E> findMinNode(Node<E> cur) {
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    @Override
    public E max() {
        Node<E> maxNode = findMaxNode(root);
        if (maxNode == null) return null;
        return maxNode.is3Node() ? maxNode.rightElement : maxNode.leftElement;
    }

    Node<E> findMaxNode(Node<E> cur) {
        while (cur.right != null) cur = cur.right;
        return cur;
    }

    @Override
    public E higher(E element) {
        return findGreaterThan(root, element, null);
    }

    E findGreaterThan(Node<E> node, E element, E result) {
        if (node == null) return result;

        // 找到第一个比目标节点大的节点, 而后去找比该元素小的 并且大于目标元素的
        if (node.leftElement.compareTo(element) > 0) {
            result = node.leftElement;
            return findGreaterThan(node.left, element, result);
        } else if (node.is3Node() && node.rightElement.compareTo(element) > 0) {
            result = node.rightElement;
            return findGreaterThan(node.mid, element, result);
        } else {
            return findGreaterThan(node.right, element, result);
        }
    }

    @Override
    public E lower(E element) {
        return findLessThan(root, element, null);
    }

    E findLessThan(Node<E> node, E element, E result) {
        if (node == null) return result;

        // 找到第一个比目标元素小的节点, 而后去找比该元素大的 并且小于目标元素的, 从而得到最接近目标元素的
        if (node.is3Node() && node.rightElement.compareTo(element) < 0) {
            result = node.rightElement;
            return findLessThan(node.right, element, result);
        } else if (node.leftElement.compareTo(element) < 0) {
            result = node.leftElement;
            return findLessThan(node.is3Node() ? node.mid : node.right, element, result);
        } else {
            return findLessThan(node.left, element, result);
        }
    }

    @Override
    public Collection<E> range(E start, E stop, int limit) {
        if (limit == -1) limit = 2000;

        Collection<E> result = new ArrayList<>();
        range(root, start, stop, limit, result);
        return result;
    }

    void range(Node<E> node, E start, E stop, int limit, Collection<E> result) {
        if (node == null || result.size() == limit) return;
        boolean nonstop = Objects.isNull(stop);

        // 持续压栈，直到遇到比起始值小的节点为止
        if (node.leftElement.compareTo(start) > 0) {
            range(node.left, start, stop, limit, result);
        }

        // 压栈结束，开始从最接近 起始值 的节点出栈，升序遍历收集
        if (result.size() == limit) return;
        if (node.leftElement.compareTo(start) >= 0 && (nonstop || node.leftElement.compareTo(stop) < 0)) {
            result.add(node.leftElement);
        }

        // 处理完左侧，如果是3节点还需要处理中间节点
        if (node.is3Node() && node.rightElement.compareTo(start) > 0) {
            range(node.mid, start, stop, limit, result);
        }

        if (result.size() == limit) return;
        if (node.is3Node() && node.rightElement.compareTo(start) >= 0 && (nonstop || node.rightElement.compareTo(stop) < 0)) {
            result.add(node.rightElement);
        }

        if (nonstop || (node.leftElement.compareTo(stop) < 0 || node.is3Node() && node.rightElement.compareTo(stop) < 0)) {
            range(node.right, start, stop, limit, result);
        }
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    boolean isBalanced(Node<E> node) {
        // 空树是平衡的
        if (node == null) return true;

        if (!node.isBalanced()) return false;

        // 2-3树是绝对平衡的, 要么子节点都不为空, 要么都为空, 绝不会出现一边有一边没有的情况
        if (node.is3Node()) {
            return isBalanced(node.left) && isBalanced(node.mid) && isBalanced(node.right);
        } else {
            return isBalanced(node.left) && isBalanced(node.right);
        }
    }

    public int getHeight() {
        return height;
    }

    private static boolean allNotNull(Object... objs) {
        for (Object obj : objs) if (obj == null) return false;
        return true;
    }

    private static boolean allNull(Object... objs) {
        for (Object obj : objs) if (obj != null) return false;
        return true;
    }

    /**
     * 当前节点必定是3节点
     * 将新节点加进来, 变成临时的4节点, 最终分裂(伸展运动)为3个2节点
     *
     * @param newMember 新成员, 必定是2节点
     * @return 分裂后的中位数节点
     */
    private Node<E> split(Node<E> cur, Node<E> newMember) {
        // 新元素和原有元素做比较, 确定新元素放的位置
        if (newMember.leftElement.compareTo(cur.leftElement) < 0) {
            /*
             * 新元素放到左侧
             *
             *            2(leftElement)
             *          /   \
             *         /     \
             *        /       \
             *       1(New)    3(rightElement)
             *      / \       / \
             *     /   \     /   \
             *    N-L  N-R  C-M  C-R
             */
            Node<E> middle = new Node<>(cur.leftElement);
            Node<E> right = new Node<>(cur.rightElement);

            // 从左插入, 中间节点是有值的, 中间节点要挂到右节点的左侧
            right.left = cur.mid;
            right.right = cur.right;

            // 左节点直接使用新成员
            middle.left = newMember;
            middle.right = right;

            return middle;
        } else if (newMember.leftElement.compareTo(cur.rightElement) < 0) {
            /*
             * 新元素放到中间
             *
             *                    2(New)
             *                  /   \
             *                 /     \
             *                /       \
             *  (leftElement)1         3(rightElement)
             *              / \       / \
             *             /   \     /   \
             *            C-L  N-L  N-R  C-R
             */
            Node<E> left = new Node<>(cur.leftElement);
            Node<E> right = new Node<>(cur.rightElement);

            // 中节点的左右元素都比左节点大, 中节点的左节点当 左节点的右节点
            left.left = cur.left;
            left.right = newMember.left;

            // 中节点的左右元素都比右节点小, 中节点的右节点当 右节点的左节点, 刚好一边分一个
            right.left = newMember.right;
            right.right = cur.right;

            newMember.left = left;
            newMember.right = right;

            return newMember;
        } else {
            /*
             * 新元素放到右侧
             *
             *                    2(rightElement)
             *                  /   \
             *                 /     \
             *                /       \
             *  (leftElement)1         3(New)
             *              / \       / \
             *             /   \     /   \
             *            C-L  C-M  N-L  N-R
             */
            Node<E> middle = new Node<>(cur.rightElement);
            Node<E> left = new Node<>(cur.leftElement);

            // 从右边插入, 中间节点是有值的, 中间的值要挂到左节点的右侧
            left.left = cur.left;
            left.right = cur.mid;

            middle.left = left;
            middle.right = newMember;

            return middle;
        }
    }

    /**
     * 当前节点必定是2节点
     * 将新节点加进来, 变成3节点
     * <p>
     * 中间节点分裂会促使当前节点继续分裂, 直到根节点
     * 如果新成员最小, 当前节点的中间节点 -> 新成员的右节点
     * 如果新成员最大, 当前节点的中间节点 -> 新成员的左节点
     *
     * @param newMember 新成员，必定是2节点
     */
    private void merge(Node<E> cur, Node<E> newMember) {
        // 新元素和原有元素做比较, 确定新元素放的位置
        if (newMember.leftElement.compareTo(cur.leftElement) < 0) {
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
        } else {
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
        }
    }

    // https://inst.eecs.berkeley.edu/~cs61bl/su14/assets/readings/jrs/23Trees.txt

    /**
     * <p>
     * 2节点: 持有一个元素和两条链接(左、右), 与标准二叉树一样
     * 3节点: 持有两个元素和三条链接(左、中、右), 按照顺序为 左链接的所有元素 < leftElement < 中链接的所有元素 < rightElement
     */
    static class Node<E extends Comparable<E>> {
        E leftElement, rightElement;
        Node<E> left, mid, right;

        public Node(E leftElement) {
            this.leftElement = leftElement;
        }

        public boolean isLeaf() {
            return allNull(left, mid, right);
        }

        public boolean is3Node() {
            return rightElement != null;
        }

        public void add(E element) {
            if (is3Node()) throw new IllegalStateException("Node already full, can not add");

            // 新元素比当前节点已有的元素要小, 需要挪一下位置
            if (element.compareTo(leftElement) < 0) {
                rightElement = leftElement;
                leftElement = element;
            } else {
                rightElement = element;
            }
        }

        public boolean isBalanced() {
            // 叶子节点一定是平衡的
            if (isLeaf()) return true;

            // 叶子节点外, 指针一定会在, 但当删除2节点时实际上是把值清空了, 节点还在, 留下一个没有元素的空白节点
            if (is3Node()) {
                return allNotNull(left, mid, right) && allNotNull(left.leftElement, mid.leftElement, right.leftElement);
            } else {
                return allNotNull(left, right) && allNotNull(left.leftElement, right.leftElement);
            }
        }

        /*
         * DELETE OPERATIONS IS ALL BELOW.
         *
         * 2-3树是完美平衡树，删除操作的基本原则是不影响层高，保持完美平衡的代价非常昂贵。
         *
         * 删除后的修复需要分3个层面来看待问题。
         *
         * 1. 删除的节点层面
         *
         * 如果是3节点，那么直接删除即可。如果是2节点，那么该节点变为空白节点，等待更上层去修复。
         *
         * 2. 删除节点的父节点层面
         *
         * 如果发现存在空白孩子，那么尝试修复他，具体措施是从3节点借。如果大家全都是2节点，那么会造成坍塌，等待更上层去修复。
         *
         * 3. 祖宗节点层面
         *
         * 如果发现子节点间出现坍塌，则在
         */

        /**
         * 第二层面: 删除节点的父节点视角
         * <p>
         * 在树的第二深层面尝试重新平衡最深层
         * <p>
         * 该算法尝试修复空白子节点，具体做法是从3节点借。
         * 但这棵树如果是3个2节点，那么会造成坍塌层高减一，此时只能让更高层次去尝试修复。
         */
        private void rebalanced() {
            // 基本思路是把失衡节点调整到右侧，期间如果有一个兄弟节点是3节点则可以恢复平衡
            // 到右侧之后如果是3个2节点，那么只能坍塌压缩层高，本层面已经无能为力交由更高层次去修复
            while (!isBalanced()) {
                if (is3Node()) {
                    // 左节点失衡
                    if (left.leftElement == null) {
                        // 把父节点左移拉下来顶替失衡节点，兄弟节点左移上浮顶替父节点
                        // 当然，如果中间兄弟是2节点，那么中间节点会变成空白节点，这没关系，下一轮平衡中会尝试修复它
                        left.leftElement = leftElement;
                        leftElement = leftMove(mid);
                    }
                    // 中间节点失衡
                    else if (mid.leftElement == null) {
                        mid.leftElement = rightElement;
                        rightElement = leftMove(right);
                    }
                    // 右节点失衡
                    else if (right.leftElement == null) {
                        // 删除最大值时会出现这种情况，如果中间或者左兄弟是3有一个是3节点，那么可以恢复平衡。
                        if (mid.is3Node()) {
                            right.leftElement = rightElement;
                            rightElement = rightMove(mid);
                        } else if (left.is3Node()) {
                            // 连续移动两次
                            right.leftElement = rightElement;
                            rightElement = rightMove(mid);

                            mid.leftElement = leftElement;
                            leftElement = rightMove(left);
                        } else {
                            /*
                             *   5,7             5
                             *  / | \    --->   / \
                             * 4  6  N         4  6,7
                             *
                             * 借无可借，好在当前节点是3节点，把父节点变为2节点，层高不受影响
                             */
                            mid.rightElement = rightElement;
                            rightElement = null;
                            right = mid;
                            mid = null;
                        }
                    }
                } else {
                    // 左节点失衡
                    if (left.leftElement == null) {
                        left.leftElement = leftElement;
                        leftElement = leftMove(right);
                    }
                    // 右节点失衡
                    else if (right.leftElement == null) {
                        if (left.is3Node()) {
                            right.leftElement = leftElement;
                            leftElement = rightMove(left);
                        } else {
                            /*
                             *   5            5,6
                             *  / \    --->
                             * 6   N
                             *
                             * 3个二节点的情况无法修复，只能先压缩层高(坍塌)，交由上层处理。
                             */
                            rightElement = leftElement;
                            leftElement = left.leftElement;

                            left = mid = right = null;
                        }
                    }
                }
            }
        }

        private E borrowMin() {
            E borrowed;
            if (!isLeaf()) {
                borrowed = left.borrowMin();
            } else {
                borrowed = leftElement;
                leftElement = null;

                if (is3Node()) {
                    leftElement = rightElement;
                    rightElement = null;
                }
            }

            rebalanced();
            return borrowed;
        }

        private E borrowMax() {
            E borrowed;
            if (!isLeaf()) {
                borrowed = right.borrowMax();
            } else {
                if (is3Node()) {
                    borrowed = rightElement;
                    rightElement = null;
                } else {
                    borrowed = leftElement;
                    leftElement = null;
                }
            }

            rebalanced();
            return borrowed;
        }
    }

    private static <E extends Comparable<E>> E leftMove(Node<E> node) {
        E min = node.leftElement;

        // 如果当前节点是3节点右两个元素，那么借出小的元素，日子还能过下去
        if (node.is3Node()) {
            node.leftElement = node.rightElement;
            node.rightElement = null;
        }
        // 把仅有的一个元素借出去，会变成空白节点
        // 父级节点会观察到这一点，可能会在下一轮平衡中修复
        else {
            node.leftElement = null;
        }

        return min;
    }

    private static <E extends Comparable<E>> E rightMove(Node<E> node) {
        E max;

        if (node.is3Node()) {
            max = node.rightElement;
            node.rightElement = null;
        } else {
            max = node.leftElement;
            node.leftElement = null;
        }

        return max;
    }

    @Override
    public E removeMin() {
        E min = deleteMin(root);
        if (min != null) {
            size--;
            if (root.leftElement == null) root = null;
        }

        return min;
    }

    E deleteMin(Node<E> cur) {
        if (cur == null) return null;

        E min;
        if (cur.left != null) {
            min = deleteMin(cur.left);
        } else {
            min = cur.leftElement;
            if (cur.is3Node()) {
                cur.leftElement = cur.rightElement;
                cur.rightElement = null;
            } else {
                // 当前节点变为空白节点 由父节点去平衡
                cur.leftElement = null;
            }

            return min;
        }

        // 叶子节点必须重新平衡一次
        // 若都是2节点的情况下会压缩层高，对此子级已经无能为力
        cur.rebalanced();

        if (!cur.isLeaf()) {
            int balanced = 0;
            while (balanced++ >= 0) {
                if (cur.is3Node()) {
                    // 左孩子发生了坍塌
                    if (cur.left.isLeaf() && !cur.mid.isLeaf()) {
                        // 从右侧最邻近的兄弟借最小值
                        E borrowed = cur.mid.borrowMin();
                        E readdition = cur.leftElement;

                        // 借来的最小值顶替当前节点的左元素
                        cur.leftElement = borrowed;

                        // 当前元素则重新添加一次, 会使底下坍塌的节点重新展开
                        cur.left = insert(cur, readdition, false);
                    }
                    // 中间孩子发生了坍塌
                    else if (cur.mid.isLeaf() && !cur.right.isLeaf()) {
                        E borrowed = cur.right.borrowMin();
                        E readdition = cur.rightElement;

                        cur.rightElement = borrowed;
                        cur.mid = insert(cur, readdition, false);
                    }
                    // 右孩子发生了坍塌
                    else if (!cur.mid.isLeaf() && cur.right.isLeaf()) {
                        E borrowed = cur.mid.borrowMax();
                        E readdition = cur.rightElement;

                        if (cur.mid.isLeaf()) {
                            cur.rightElement = borrowed;
                            cur.right = insert(cur, readdition, false);

                            borrowed = cur.mid.borrowMax();
                            readdition = cur.rightElement;

                            cur.rightElement = borrowed;
                            insert(cur, readdition, true);

                            borrowed = cur.mid.borrowMax();
                            readdition = cur.rightElement;

                            cur.rightElement = borrowed;
                            insert(cur, readdition, true);
                            insert(cur, cur.rightElement, true);
                            cur.rightElement = null;

                            cur.mid = null;
                            continue;
                        }

                        /*
                         *     5   ,   9
                         *    /    |    \
                         *   3     7   10,11
                         *  / \   / \
                         * 2   4 6   8
                         *
                         *      5   ,   9
                         *     /    |    \
                         *    3    6,7  10,11
                         *   / \
                         *  2   4
                         *
                         *     5   ,   8
                         *    /    |    \
                         *   3    6,7    10
                         *  / \          / \
                         * 2   4        9   11
                         *
                         *     5   ,   7
                         *    /    |    \
                         *   3     6     10
                         *  / \          / \
                         * 2   4       8,9   11
                         *
                         *     5   ,   6
                         *    /         \
                         *   3           10
                         *  / \          / \
                         * 2   4      7,8,9   11
                         *
                         *      5
                         *    /   \
                         *   3     8,10
                         *  / \   /  |  \
                         * 2   4 6,7 9   11
                         */
                        cur.rightElement = borrowed;
                        cur.right = insert(cur, readdition, false);
                    } else balanced = -1;
                } else {
                    // 左孩子发生了坍塌
                    if (cur.left.isLeaf() && !cur.right.isLeaf()) {
                        E borrowed = cur.right.borrowMin();

                        /*
                         *    4                5                  4
                         *   / \              / \                / \
                         * 2,3  6    --->    3  6,7    --->    2,3  6    --->   ...
                         *     / \          / \                    / \
                         *    5   7        2   4                  5   7
                         *
                         *    4,6
                         *   / | \
                         * 2,3 5  7
                         */
                        if (cur.right.isLeaf()) {
                            cur.mid = new Node<>(borrowed);
                            cur.rightElement = cur.right.borrowMin();
                            continue;
                        }

                        E readdition = cur.leftElement;

                        cur.leftElement = borrowed;
                        cur.left = insert(cur, readdition, false);
                    }
                    // 右孩子发生了坍塌
                    else if (!cur.right.isLeaf() && cur.left.isLeaf()) {
                        E borrowed = cur.left.borrowMax();

                        /*
                         *     5                  4
                         *    / \                / \
                         *   3  6,7    --->    2,3  6
                         *  / \                    / \
                         * 2   4                  5   7
                         *
                         *   3,5
                         *  / | \
                         * 2  4 6,7
                         */
                        if (cur.left.isLeaf()) {
                            cur.mid = new Node<>(borrowed);
                            cur.rightElement = cur.leftElement;
                            cur.leftElement = cur.left.borrowMax();
                            continue;
                        }

                        E readdition = cur.leftElement;

                        cur.leftElement = borrowed;
                        cur.right = insert(cur, readdition, false);
                    } else balanced = -1;
                }

                if (balanced > 10) throw new RuntimeException();
            }
        }

        return min;
    }

    @Override
    public E removeMax() {
        return null;
    }

    @Override
    public boolean remove(E e) {
        return false;
    }
}
