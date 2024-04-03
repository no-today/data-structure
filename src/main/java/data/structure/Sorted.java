package data.structure;

/**
 * 时间复杂度为 O(logn)
 *
 * @author no-today
 * @date 2024/03/27 09:12
 */
public interface Sorted<E extends Comparable<E>> extends Collection<E> {

    @Override
    default boolean contains(Object e) {
        try {
            return contains((E) e);
        } catch (ClassCastException ex) {
            return false;
        }
    }

    boolean contains(E e);

    /**
     * 获取集合中的最小元素
     */
    E min();

    /**
     * 获取集合中的最大元素
     */
    E max();

    /**
     * 删除最小元素
     */
    E removeMin();

    /**
     * 删除最大元素
     */
    E removeMax();

    /**
     * 返回集合中严格大于指定元素的最小元素
     */
    E higher(E element);

    /**
     * 返回集合中严格小于指定元素的最大元素
     */
    E lower(E element);

    /**
     * 获取范围内的N个元素
     *
     * @param start 起始值(包含)
     * @param stop  结束值(不包含) null 为不限制
     * @param limit 数量 -1 为不限制
     * @return 范围内的 limit 个元素
     */
    Collection<E> range(E start, E stop, int limit);
}
