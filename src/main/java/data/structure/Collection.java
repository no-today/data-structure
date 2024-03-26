package data.structure;

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

    default Object[] toArray() {
        return toArray((E[]) new Object[0]);
    }

    E[] toArray(E[] arr);
}
