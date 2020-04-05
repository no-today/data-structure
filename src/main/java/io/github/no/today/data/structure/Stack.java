package io.github.no.today.data.structure;

/**
 * @author no-today
 * @date 2018/6/21
 */
public interface Stack<E> {

    /**
     * Push
     *
     * @param element element
     */
    void push(E element);

    /**
     * Pop out, get top element and remove
     *
     * @return top element
     */
    E pop();

    /**
     * Get top element
     *
     * @return top element
     */
    E peek();

    /**
     * Get size
     *
     * @return size
     */
    int size();

    /**
     * Is empty
     *
     * @return empty
     */
    boolean isEmpty();

    /**
     * Clear all element
     */
    void clear();
}
