package data.structure;

/**
 * @author no-today
 * @date 2018/9/29
 */
public interface Set<element> {

    /**
     * Add to
     *
     * @param element element
     */
    void add(element element);

    /**
     * Remove by element
     *
     * @param element element
     */
    void remove(element element);

    /**
     * Check existence
     *
     * @param element element
     * @return exists
     */
    boolean contains(element element);

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
