package data.structure;

/**
 * @author no-today
 * @date 2018/9/19
 */
public interface Map<K, V> {

    /**
     * Add / update
     *
     * @param key   key
     * @param value value
     * @return Covered element
     */
    V put(K key, V value);

    /**
     * Remove by key
     *
     * @param key key
     * @return removed element
     */
    V remove(K key);

    /**
     * Get by key
     *
     * @param key key
     * @return value
     */
    V get(Object key);

    V getOrDefault(Object key, V defaultValue);

    /**
     * Existence key
     *
     * @param key key
     * @return exists
     */
    boolean containsKey(Object key);

    /**
     * Existence value
     *
     * @param value value
     * @return exists
     */
    boolean containsValue(Object value);

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

    /**
     * Convert to set
     *
     * @return set
     */
    Set<Entry<K, V>> entrySet();

    Set<K> entryKey();

    Collection<V> values();

    /**
     * Key-value pair
     *
     * @param <K>
     * @param <V>
     */
    interface Entry<K, V> {
        /**
         * Get key
         *
         * @return key
         */
        K getKey();

        /**
         * Get value
         *
         * @return val
         */
        V getValue();
    }
}
