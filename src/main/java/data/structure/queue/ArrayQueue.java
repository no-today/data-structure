package data.structure.queue;

import data.structure.Queue;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 循环数组实现 最后一个元素不可用
 * <p>
 * 队列为空: head == tail
 * 队列已满: (tail + 1) % capacity == head
 */
public class ArrayQueue<E> implements Queue<E> {

    /**
     * Default size
     */
    private static final int DEFAULT_CAPACITY = 8;

    private E[] elements;

    /**
     * head :下一个出队的元素位置
     * tail : (headIndex + count - 1) mod capacity
     */
    private int head, size = 0;

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayQueue(int capacity) {
        this.elements = (E[]) new Object[capacity];
    }

    @Override
    public boolean add(E e) {
        enqueue(e);
        return true;
    }

    @Override
    public boolean remove(E e) {
        for (int i = head; i < head + size; i++) {
            if (Objects.equals(elements[i], e)) {
                // left shift
                for (int j = i; j < size; j++) {
                    elements[j] = elements[j + 1];
                }
                size--;
                return true;
            }
        }

        return false;
    }

    /**
     * head == tail 队列为空
     * tail % capacity + 1 == head 队列已满
     *
     * @param element new element
     */
    @Override
    public void enqueue(E element) {
        resizeCapacity();

        elements[(head + size) % elements.length] = element;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        E element = elements[head % elements.length];
        elements[head % elements.length] = null;
        head++;
        size--;

        return element;
    }

    @Override
    public E front() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        return elements[head % elements.length];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object e) {
        for (int i = head; i < head + size; i++) {
            if (Objects.equals(elements[i], e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = head; i < head + size; i++) {
            elements[i % elements.length] = null;
        }

        head = size = 0;
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        int index = 0;
        for (int i = head; i < head + size; i++) {
            consumer.accept(elements[i % elements.length], index++);
        }
    }

    @Override
    public E[] toArray(E[] arr) {
        if (arr.length < size) {
            arr = (E[]) Array.newInstance(arr.getClass().getComponentType(), size);
        }

        E[] finalArr = arr;
        foreach((e, i) -> finalArr[i] = e);
        return finalArr;
    }

    private void resizeCapacity() {
        // 队列已满, 扩容
        if (size == elements.length) {
            int capacity = elements.length * 2;

            E[] newElements = (E[]) new Object[capacity];
            System.arraycopy(elements, head, newElements, 0, size);
            elements = newElements;
        }
    }
}
