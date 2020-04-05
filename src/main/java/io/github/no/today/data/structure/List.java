package io.github.no.today.data.structure;

/**
 * @author no-today
 * @date 2018/6/26
 */
public interface List<E> {

    /**
     * Add
     *
     * :array:  O(1), when full O(N)
     * :linked: O(1)
     *
     * @param element element
     */
    void add(E element);

    /**
     * Add in first
     *
     * :array:  O(n)
     * :linked: O(1)
     *
     * @param element element
     */
    void addFirst(E element);

    /**
     * Add in tail
     *
     * :array:  O(1), when full O(n)
     * :linked: O(1)
     *
     * @param element element
     */
    void addLast(E element);

    /**
     * Remove by index
     *
     * :array:  O(n)
     * :linked: O(1)
     *
     * @param index index
     * @return removed element
     */
    E remove(int index);

    /**
     * Remove by element
     *
     * :array:  O(n)
     * :linked: 0(1)
     *
     * @param element element
     * @return remove success
     */
    boolean remove(E element);

    /**
     * Delete first
     *
     * :array:  O(n)
     * :linked: 0(1)
     *
     * @return removed element
     */
    E removeFirst();

    /**
     * Delete tail
     *
     * :array:  O(1)
     * :linked: 0(1)
     *
     * @return removed element
     */
    E removeLast();

    /**
     * Update
     *
     * :array:  O(1)
     * :linked: 0(n)
     *
     * @param index   index
     * @param element element
     * @return Covered element
     */
    E set(int index, E element);

    /**
     * Get by index
     *
     * :array:  O(1)
     * :linked: 0(n)
     *
     * @param index index
     * @return element
     */
    E get(int index);

    /**
     * Element exists
     *
     * :array:  O(n)
     * :linked: 0(n)
     *
     * @param element element
     * @return exists
     */
    boolean contains(E element);

    /**
     * Get the index of the element
     *
     * :array:  O(n)
     * :linked: 0(n)
     *
     * @param element element
     * @return index
     */
    int indexOf(E element);

    /**
     * Get the index of the element(Reverse order)
     *
     * :array:  O(n)
     * :linked: 0(n)
     *
     * @param element element
     * @return index index
     */
    int lastIndexOf(E element);

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
     * To array
     *
     * @return array
     */
    Object[] toArray();

    /**
     * Packed into the passed in array, if not fit it will return a new array
     *
     * @param array array
     * @return array
     */
    E[] toArray(E[] array);
}
