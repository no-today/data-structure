package data.structure;

/**
 * Double-end queue
 *
 * @author no-today
 * @date 2024/03/25 16:56
 */
public interface Deque<E> extends Collection<E> {

    void offerFirst(E e);

    void offerLast(E e);

    E peekFirst();

    E peekLast();

    E pollFirst();

    E pollLast();
}
