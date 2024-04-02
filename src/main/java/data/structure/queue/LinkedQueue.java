package data.structure.queue;

import data.structure.Queue;
import data.structure.list.LinkedList;

import java.util.function.BiConsumer;

/**
 * @author no-today
 * @date 2020/3/22 10:00
 */
public class LinkedQueue<E> implements Queue<E> {

    private final LinkedList<E> linkedList;

    public LinkedQueue() {
        this.linkedList = new LinkedList<>();
    }

    @Override
    public void enqueue(E element) {
        linkedList.add(element);
    }

    @Override
    public E dequeue() {
        return linkedList.removeFirst();
    }

    @Override
    public E front() {
        if (linkedList.isEmpty()) return null;
        return linkedList.get(0);
    }

    @Override
    public int size() {
        return linkedList.size();
    }

    @Override
    public boolean contains(Object e) {
        return linkedList.contains(e);
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        linkedList.foreach(consumer);
    }

    @Override
    public E[] toArray(E[] arr) {
        return linkedList.toArray(arr);
    }

    @Override
    public boolean add(E e) {
        enqueue(e);
        return true;
    }

    @Override
    public boolean remove(E e) {
        return linkedList.remove(e);
    }

    @Override
    public void clear() {
        linkedList.clear();
    }

    @Override
    public String toString() {
        return linkedList.toString();
    }
}
