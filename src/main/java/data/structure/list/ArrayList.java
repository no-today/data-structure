package data.structure.list;

import data.structure.List;

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
     * Initial capacity, Total capacity
     */
    private int initCapacity, capacity;

    /**
     * Effective element
     */
    private int size;

    /**
     * Shrinkage factor < 4/1
     */
    private static final float SHRINKAGE_COEFFICIENT = 0.25F;

    public ArrayList() {
        capacity = initCapacity = DEFAULT_CAPACITY;
        elements = (E[]) new Object[initCapacity];
    }

    public ArrayList(int capacity) {
        this.capacity = initCapacity = capacity;
        elements = (E[]) new Object[initCapacity];
    }

    public ArrayList(E[] array) {
        capacity = initCapacity = array.length;
        elements = (E[]) new Object[initCapacity];
        for (int i = 0; i < array.length; i++) {
            elements[i] = array[i];
        }
        size = array.length;
    }

    @Override
    public void add(E element) {
        resizeCapacity();

        elements[size++] = element;
    }

    @Override
    public void addFirst(E element) {
        resizeCapacity();

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

        resizeCapacity();

        return element;
    }

    @Override
    public boolean remove(E element) {
        boolean flag = false;

        if (element == null) {
            return flag;
        }


        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) {
                leftShift(i);
                size--;
                flag = true;
            }
        }

        return flag;
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
    public boolean contains(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int indexOf(E element) {
        int index = -1;

        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return i;
            }
        }

        return index;
    }

    @Override
    public int lastIndexOf(E element) {
        int index = -1;

        for (int i = size - 1; i >= 0; i--) {
            if (elements[i].equals(element)) {
                return i;
            }
        }

        return index;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
        capacity = initCapacity;
        copyArray();
    }

    @Override
    public Object[] toArray() {
        E[] array = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = elements[i];
        }
        return array;
    }

    @Override
    public E[] toArray(E[] array) {
        if (array == null || array.length < size) {
            array = (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }

        for (int i = 0; i < size; i++) {
            array[i] = elements[i];
        }
        return array;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(elements[i]);
            } else {
                sb.append(elements[i]).append(", ");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public int realSize() {
        return capacity;
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
    private void resizeCapacity() {
        /*
         * 满了才扩容
         *
         * 小于四分之一缩容
         * 1. 容量小于初始化容量时不做缩容
         */
        if (size == capacity - 1) {
            capacity = capacity * 2;
            copyArray();
        } else if (capacity > initCapacity && size < capacity * SHRINKAGE_COEFFICIENT) {
            capacity = capacity / 2;
            copyArray();
        }
    }

    /**
     * O(n)
     */
    private void copyArray() {
        E[] newElements = (E[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }
}
