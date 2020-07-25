package data.structure.queue;

import data.structure.Queue;
import data.structure.list.LinkedList;

/**
 * @author no-today
 * @date 2020/3/22 10:00
 */
public class LinkedQueue<E> implements Queue<E> {

    private LinkedList<E> linkedList;

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
        return linkedList.get(0);
    }

    @Override
    public int size() {
        return linkedList.size();
    }

    @Override
    public boolean isEmpty() {
        return linkedList.isEmpty();
    }
}
