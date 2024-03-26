package data.structure.list;

import data.structure.List;

import java.util.Objects;

/**
 * @author no-today
 * @date 2018/6/20
 */
public class ArrayList<E> implements List<E> {

    /**
     * Default size
     */
    private static final int DEFAULT_CAPACITY = 8;

    /**
     * Element domain
     */
    private E[] elements;

    /**
     * Actual capacity
     */
    private int capacity;

    /**
     * Used capacity
     */
    private int size;

    private int resizeCount;

    public ArrayList(E[] array) {
        capacity = array.length;
        elements = (E[]) new Object[capacity];
        System.arraycopy(array, 0, elements, 0, array.length);
        size = array.length;
    }

    public ArrayList(int capacity) {
        this.capacity = capacity;
        elements = (E[]) new Object[capacity];
    }

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public boolean add(E element) {
        checkResizeCapacity();

        elements[size++] = element;
        return true;
    }

    @Override
    public void addFirst(E element) {
        checkResizeCapacity();

        rightShift(0);
        elements[0] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        add(element);
    }

    @Override
    public E remove(int index) {
        checkIndexOut(index);

        E element = elements[index];
        leftShift(index);
        size--;

        return element;
    }

    @Override
    public boolean remove(E element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], element)) {
                leftShift(i);
                size--;
                return true;
            }
        }

        return false;
    }

    @Override
    public E removeFirst() {
        E element = elements[0];
        leftShift(0);
        size--;
        return element;
    }

    @Override
    public E removeLast() {
        E element = elements[size - 1];
        elements[--size] = null;
        return element;
    }

    @Override
    public E set(int index, E element) {
        checkIndexOut(index);

        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    @Override
    public E get(int index) {
        checkIndexOut(index);

        return elements[index];
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int indexOf(E element) {
        int index = -1;

        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], element)) {
                return i;
            }
        }

        return index;
    }

    @Override
    public int lastIndexOf(E element) {
        int index = -1;

        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(elements[i], element)) {
                return i;
            }
        }

        return index;
    }

    @Override
    public int size() {
        return size;
    }

    public int getResizeCount() {
        return resizeCount;
    }

    @Override
    public void clear() {
        size = 0;
        capacity = DEFAULT_CAPACITY;
        elements = (E[]) new Object[capacity];
    }

    @Override
    public E[] toArray(E[] array) {
        if (array == null || array.length < size) {
            array = (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }

        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(elements[i]);
            } else {
                sb.append(elements[i]).append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public int realSize() {
        return capacity;
    }

    public void swap(int i, int j) {
        E ej = set(i, get(j));
        set(j, ej);
    }

    /**
     * Detect index out of bounds
     *
     * @param index index
     */
    private void checkIndexOut(int index) {
        if (index < 0 || index > capacity - 1) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * O(n)
     *
     * @param index index
     */
    private void leftShift(int index) {
        for (int i = index; i < size; i++) {
            elements[i] = elements[i + 1];
        }
    }

    /**
     * O(n)
     *
     * @param index index
     */
    private void rightShift(int index) {
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
    }

    /**
     * Reallocate memory size
     */
    private void checkResizeCapacity() {
        /*
         * 满了才扩容
         *
         * 小于四分之一缩容
         * 1. 容量小于初始化容量时不做缩容
         */
        if (size == capacity - 1) {
            capacity = capacity * 2;

            E[] newElements = (E[]) new Object[capacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;

            resizeCount++;
        }
    }
}
