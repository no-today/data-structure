package data.structure;

import java.util.function.BiConsumer;

/**
 * @author no-today
 * @date 2024/03/25 10:56
 */
public interface Collection<E> {

    /**
     * Returns: true if this collection changed as a result of the call
     */
    boolean add(E e);

    /**
     * Returns: true if this collection changed as a result of the call
     */
    boolean remove(E e);

    void clear();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    boolean contains(Object e);

    void foreach(BiConsumer<E, Integer> consumer);

    E[] toArray(E[] arr);

    default String tostring() {
        StringBuilder sb = new StringBuilder("[");
        foreach((e, i) -> sb.append(e).append(i == size() - 1 ? "]" : ", "));
        return sb.toString();
    }
}
