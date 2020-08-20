package data.structure.queue;

import data.structure.Queue;

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
    private int head, count = 0;

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayQueue(int capacity) {
        this.elements = (E[]) new Object[capacity];
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

        elements[(head + count) % elements.length] = element;
        count++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        E element = elements[head % elements.length];
        elements[head % elements.length] = null;
        head++;
        count--;

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
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public void clear() {
        for (int i = head; i < head + count; i++) {
            elements[i % elements.length] = null;
        }

        head = count = 0;
    }

    private void resizeCapacity() {
        // 队列已满, 扩容
        if (count == elements.length) {
            int capacity = elements.length * 2;

            E[] newElements = (E[]) new Object[capacity];
            System.arraycopy(elements, head, newElements, 0, count);
            elements = newElements;
        }
    }
}
