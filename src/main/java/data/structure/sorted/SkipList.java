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
 * <p>
 * 跳表是一种多层级的有序列表结构，每个节点具有多个指向下一个节点的引用。通过多级索引，跳表能够在每一层上跳过部分元素，
 * 使得搜索、插入和删除操作的平均时间复杂度为 O(log n)。
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

    /**
     * 最大缓存层 该参数直接影响跳表的性能和空间占用
     */
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
        int cacheLevel = randomIndexLevel();
        // refresh currentLevel
        currentLevel = Math.max(currentLevel, cacheLevel);

        // 从上往下查找插入的位置
        SkipListNode<E> newNode = new SkipListNode<>(element, cacheLevel);
        SkipListNode<E> cur = root;
        for (int level = currentLevel - 1; level >= 0; level--) {
            cur = findInsertPositionInCurrentLayer(cur, level, element);

            // 从上往下找是为了搜索效率, 但并不是每一层都需要插入, 只需要添加到底部的 level 层
            if (level < cacheLevel) {
                // 添加新元素
                newNode.next[level] = cur.next[level];
                cur.next[level] = newNode;
            }

            // 添加完当前层之后下降一层, 从当前位置开始查找该层的插入位置, 无需从该层的 ROOT 节点开始找
        }

        length++;
        return true;
    }

    @Override
    public boolean contains(E element) {
        SkipListNode<E> cur = root;
        for (int level = currentLevel - 1; level >= 0; level--) {
            cur = findInsertPositionInCurrentLayer(cur, level, element);

            // 如果找的目标比所有的的大, 那么 cur 已经是链表尾部了(指向空)
            if (cur.next[level] != null && cur.next[level].element.compareTo(element) == 0) {
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
        for (int level = currentLevel - 1; level >= 0; level--) {
            cur = findInsertPositionInCurrentLayer(cur, level, element);

            if (cur.next[level] != null && cur.next[level].element.compareTo(element) == 0) {
                SkipListNode<E> removeNode = cur.next[level];
                cur.next[level] = removeNode.next[level];
                removeNode.next[level] = null;

                // 每个元素只需要计数一次
                if (level == 0) {
                    deleted = true;
                    length--;
                }

                // 只删除第一个匹配的而不是全部
                // level++;
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

    /*
     * 1       5
     * 1   3   5
     * 1 2 3 4 5
     *
     * higher 3 -> 4
     */
    @Override
    public E higher(E element) {
        if (isEmpty()) return null;

        // 从顶层开始向下查找, 一定要查到最底层
        SkipListNode<E> cur = root;
        for (int level = currentLevel - 1; level >= 0; level--) {
            while (cur.next[level] != null && cur.next[level].element.compareTo(element) <= 0) {
                cur = cur.next[level];
            }
        }

        if (cur.next[0] != null) {
            return cur.next[0].element;
        }

        return null;
    }

    /*
     * 1       5
     * 1   3   5
     * 1 2 3 4 5
     *
     * lower 3 -> 2
     */
    @Override
    public E lower(E element) {
        if (isEmpty()) return null;

        // 从顶层开始向下查找, 一定要查到最底层
        SkipListNode<E> cur = root;
        for (int level = currentLevel - 1; level >= 0; level--) {
            cur = findInsertPositionInCurrentLayer(cur, level, element);
        }

        // 如果没有比目标元素更小的，cur 为 ROOT，element 是空
        return cur.element;
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
        for (int level = currentLevel - 1; level >= 0; level--) {
            cur = findInsertPositionInCurrentLayer(cur, level, start);

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
            if (stop != null && cur.element.compareTo(stop) >= 0) {
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
     * 在当前层寻找插入位置: 插入在返回节点的右侧(返回的结果必定比目标节点小)
     * <p>
     * 我的右节点比 目标元素 大, 说明 目标 应该位于 我 和 我的右节点 的 中间
     * <p>
     * 返回的节点可能与 element 所处节点的值相同, 因为右节点必定是 大于的
     */
    private SkipListNode<E> findInsertPositionInCurrentLayer(SkipListNode<E> node, int level, E element) {
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

    public int getLevel() {
        return currentLevel;
    }

    /**
     * ___
     * |  |                                          ->                                     | NIL |
     * |  | -> | 0 |                                 ->                                     | NIL |
     * |  | -> | 0 |           ->        | 50 |                     ->                      | NIL |
     * |  | -> | 0 |           ->        | 50 |      ->      | 75 |           ->            | NIL |
     * |__| -> | 0 | -> | 25 | -> | 40 | | 50 | -> | 60 | -> | 75 | -> | 80 | -> | 100 | -> | NIL |
     * <p>
     * ROOT 节点是特殊的，它需要能从所有缓存层开始访问 next，所以持有所有层级的 next。
     * 其他节点在新增的时候会随机缓存 N 层(1 <= N < maxLevel)
     * <p>
     * 访问当前层的下一个节点: node.next[node.next.length - 1]
     * 从当前节点的视角来看，可以看到下一个节点全部层级，但是看不到自己的下层。
     */
    private static class SkipListNode<E> {

        private final E element;
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
            return "{" + "score=" + element + ", levels=" + next.length + '}';
        }
    }
}
