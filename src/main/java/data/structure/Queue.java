package data.structure;

/**
 * @author no-today
 * @date 2020/3/21 23:47
 */
public interface Queue<E> extends Collection<E> {

    /**
     * Add element to queue
     *
     * @param element new element
     */
    void enqueue(E element);

    /**
     * Get first element and remove
     *
     * @return first element
     */
    E dequeue();

    /**
     * Get first element
     *
     * @return first element
     */
    E front();
}
