package data.structure.sorted;

import data.structure.Collection;
import data.structure.List;
import data.structure.Sorted;
import data.structure.list.ArrayList;

import java.lang.reflect.Array;
import java.util.function.BiConsumer;

/**
 * 跳跃列表
 * <p>
 * 参考 Redis 实现
 *
 * @author no-today
 * @date 2022/04/30 14:25
 */
public class SkipList<E extends Comparable<E>> implements Sorted<E> {

    private static final int DEFAULT_MAX_LEVEL = 16;

    /**
     * 预期 N + 1 层
     */
    private static final double DEFAULT_P_FACTOR = 0.5;

    private final int maxLevel;
    private final double pFactor;

    private int currentLevel;

    private int length;

    private SkipListNode<E> root;

    public SkipList() {
        this(DEFAULT_MAX_LEVEL, DEFAULT_P_FACTOR);
    }

    public SkipList(int maxLevel, double pFactor) {
        this.maxLevel = maxLevel;
        this.pFactor = Math.min(pFactor, DEFAULT_P_FACTOR); // 索引太多没意义

        this.currentLevel = 1;
        this.root = new SkipListNode<>(null, maxLevel);
    }

    /**
     * 新增元素
     * <p>
     * 1. 每个新增元素都将随机存在于 N 层
     * 2. 从上往下, 更新每一层的指向, 把新元素加进去
     * <p>
     * 寻找插入的位置:
     * 因为有特殊的 ROOT(head) 节点兜底, 区别于普通链表, 不可能出现插入到 ROOT 节点左边的情况, 所以我们应该找到插入位置的左侧节点.
     * <p>
     * 当前节点的右节点比新增元素大, 说明应该在 当前节点 和 当前节点的右节点中间插入新节点.
     * while cur.next != null and new < cur.next: cur = cur.next
     */
    public boolean add(E element) {
        // 随机索引 N 层
        int level = randomIndexLevel();
        // refresh currentLevel
        currentLevel = Math.max(currentLevel, level);

        // 从上往下查找插入的位置
        SkipListNode<E> newNode = new SkipListNode<>(element, level);
        SkipListNode<E> cur = root;
        for (int i = currentLevel - 1; i >= 0; i--) {
            cur = findInsertPosition(cur, i, element);

            // 从上往下找是为了搜索效率, 但并不是每一层都需要插入, 只需要添加到底部的 level 层
            if (i < level) {
                // 添加新元素
                newNode.next[i] = cur.next[i];
                cur.next[i] = newNode;
            }

            // 添加完当前层之后下降一层, 从当前位置开始查找该层的插入位置, 无需从该层的 ROOT 节点开始找
        }

        length++;
        return true;
    }

    public boolean contains(E element) {
        SkipListNode<E> cur = root;
        for (int i = currentLevel - 1; i >= 0; i--) {
            cur = findInsertPosition(cur, i, element);

            // 如果找的目标比所有的的大, 那么 cur 已经是链表尾部了(指向空)
            if (cur.next[i] != null && cur.next[i].element.compareTo(element) == 0) {
                return true;
            }

            // 如果当前层没有, 下落一层继续查找
        }
        return false;
    }

    /**
     * 删除元素: 反添加操作
     */
    public boolean remove(E element) {
        boolean deleted = false;
        SkipListNode<E> cur = root;
        for (int i = currentLevel - 1; i >= 0; i--) {
            cur = findInsertPosition(cur, i, element);

            if (cur.next[i] != null && cur.next[i].element.compareTo(element) == 0) {
                SkipListNode<E> removeNode = cur.next[i];
                cur.next[i] = removeNode.next[i];
                removeNode.next[i] = null;

                // 每个元素只需要计数一次
                if (i == 0) {
                    deleted = true;
                    length--;
                }

                // 只删除第一个匹配的而不是全部
                // i++;
            }
        }

        return deleted;
    }

    @Override
    public void clear() {
        currentLevel = 1;
        length = 0;
        root = new SkipListNode<>(null, maxLevel);
    }

    @Override
    public E min() {
        if (isEmpty()) return null;
        return root.next[0].element;
    }

    @Override
    public E max() {
        if (isEmpty()) return null;

        SkipListNode<E> cursor = root;
        int level = root.next.length - 1;
        while (level >= 0) {
            while (cursor.next[level] != null) {
                cursor = cursor.next[level];
            }
            // 下落一层
            level--;
        }

        return cursor.element;
    }

    @Override
    public E removeMin() {
        E min = min();
        if (min != null) remove(min);
        return min;
    }

    @Override
    public E removeMax() {
        E max = max();
        if (max != null) remove(max);
        return max;
    }

    @Override
    public E higher(E element) {
        if (isEmpty()) return null;

        return null;
    }

    @Override
    public E lower(E element) {
        if (isEmpty()) return null;

        return null;
    }

    /**
     * 获取范围内的 n 个元素(包含等于)
     *
     * @param start 起始值
     * @param stop  结束值, -1 为不限制
     * @param limit 数量, -1 为不限制
     * @return 范围内的 n 个元素
     */
    public Collection<E> range(E start, E stop, int limit) {
        if (limit == -1) limit = Integer.MAX_VALUE;

        List<E> array = new ArrayList<>();

        SkipListNode<E> cur = root;
        for (int i = currentLevel - 1; i >= 0; i--) {
            cur = findInsertPosition(cur, i, start);

            // not found
            if (cur.isRoot()) {
                continue;
            }

            // 找到首个比 start 大的节点
            if (cur.element.compareTo(start) >= 0) {
                break;
            }
        }

        // down 到底层再开始获取
        cur = cur.next[0];
        int count = 0;
        while (cur != null && count < limit) {
            // 大于停止值, 不需要再继续收集了
            if (stop != null && cur.element.compareTo(stop) > 0) {
                break;
            }

            if (cur.element.compareTo(start) >= 0) {
                array.add(cur.element);
            }

            cur = cur.next[0];
            count++;
        }

        return array;
    }

    public int size() {
        return length;
    }

    @Override
    public boolean contains(Object e) {
        return contains((E) e);
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        // 直接遍历底层
        int i = 0;
        SkipListNode<E> cur = root.next[0];
        while (cur != null) {
            consumer.accept(cur.element, i++);
            cur = cur.next[0];
        }
    }

    @Override
    public E[] toArray(E[] arr) {
        if (arr.length < length) {
            arr = (E[]) Array.newInstance(arr.getClass().getComponentType(), length);
        }

        E[] finalArr = arr;
        foreach((e, i) -> finalArr[i] = e);
        return finalArr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        // 直接遍历底层就行
        SkipListNode<E> cur = root.next[0];
        while (cur != null) {
            if (cur.next[0] == null) {
                sb.append(cur.element);
            } else {
                sb.append(cur.element).append(", ");
            }

            cur = cur.next[0];
        }

        return sb.append("]").toString();
    }

    /**
     * 寻找插入位置: 插入在返回节点的右侧(返回的接口必定比目标节点小)
     * <p>
     * 我的右节点比 目标元素 大, 说明 目标 应该位于 我 和 我的右节点 的 中间
     * <p>
     * 返回的节点可能与 element 所处节点的值相同, 因为右节点必定是 大于的
     */
    private SkipListNode<E> findInsertPosition(SkipListNode<E> node, int level, E element) {
        // 如需降序: 右节点小于等于 目标 时中断

        // 没有 next(右) 时停止
        // 右节点大于等于 目标 时中断, 返回当前节点
        while (node.next[level] != null && node.next[level].element.compareTo(element) < 0) {
            node = node.next[level];
        }
        return node;
    }

    /**
     * 随机缓存 N 层
     */
    private int randomIndexLevel() {
        int level = 1;
        while (level < maxLevel && Math.random() < pFactor) {
            level++;
        }
        return level;
    }

    private static class SkipListNode<E> {

        private final E element;

        /**
         * N 层都有不同的 next 节点
         * 第 1 层有全部的元素,
         * 第 N 层预期有 N-1 层 1/2 的元素(可以通过 DEFAULT_P_FACTOR 参数控制索引稀疏)
         * <p>
         * 首先初始化的 ROOT Node 有 MAX_LEVEL 个 next
         * 后续添加的节点将有随机 1 ~ MAX_LEVEL 个 next
         * <p>
         * ___
         * |  |                                           ->                                    | NIL |
         * |  | -> | 30 |                                 ->                                    | NIL |
         * |  | -> | 30 |           ->        | 50 |                     ->                     | NIL |
         * |  | -> | 30 |           ->        | 50 |      ->      | 70 | -> | 80 |      ->      | NIL |
         * |__| -> | 30 | -> | 40 | -> | 45 | | 50 | -> | 60 | -> | 70 | -> | 80 | -> | 90 | -> | NIL |
         * <p>
         * 可以把该属性看作是一列
         * 第一列是 ROOT 节点, 创建跳表时立刻初始化, 它有点特殊, 实际不存储值, 例子中的 ROOT 节点有 5 个 Next 节点
         * 30 节点有 4 个 Next 节点, 以此类推...
         * <p>
         * 这样关联起来之后, 任意节点都可以知道自己 N层 的 right、down 是哪个节点
         */
        private final SkipListNode<E>[] next;

        public SkipListNode(E element, int level) {
            this.element = element;
            this.next = new SkipListNode[level];
        }

        public boolean isRoot() {
            return element == null;
        }

        @Override
        public String toString() {
            String sb = "{" + "score=" + element +
                    ", levels=" + next.length +
                    '}';
            return sb;
        }
    }
}
