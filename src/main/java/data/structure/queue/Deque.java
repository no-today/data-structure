package data.structure.queue;

/**
 * 双向队列
 *
 * @author no-today
 * @date 2024/03/21 16:40
 */
public class Deque<T> {

    private Node<T> head, tail;
    private int size;

    static class Node<T> {
        Node<T> prev, next;
        T element;

        public Node(T element) {
            this.element = element;
        }
    }

    /**
     * 添加到头部
     */
    public void offerFirst(T e) {
        Node<T> newNode = new Node<>(e);

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            Node<T> oldHead = this.head;
            newNode.next = oldHead;
            oldHead.prev = newNode;

            this.head = newNode;
        }

        size++;
    }

    /**
     * 添加到尾部
     */
    public void offerLast(T e) {
        Node<T> newNode = new Node<>(e);

        if (isEmpty()) {
            this.tail = newNode;
            this.head = newNode;
        } else {
            Node<T> oldTail = this.tail;
            newNode.prev = oldTail;
            oldTail.next = newNode;

            this.tail = newNode;
        }

        size++;
    }


    public T peekFirst() {
        return this.head == null ? null : this.head.element;
    }

    public T peekLast() {
        return this.tail == null ? null : this.tail.element;
    }

    public T pollFirst() {
        if (this.head == null) return null;

        Node<T> oldHead = this.head;
        this.head = this.head.next;

        if (this.head == null) {
            this.tail = null;
        } else {
            this.head.prev = null;
        }

        size--;

        return oldHead.element;
    }

    public T pollLast() {
        if (this.tail == null) return null;

        Node<T> oldTail = this.tail;
        this.tail = oldTail.prev;

        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }

        size--;

        return oldTail.element;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public T[] toArray() {
        if (this.head == null) return null;

        T[] result = (T[]) new Object[size];
        Node<T> each = this.head;
        int i = 0;
        while (each != null) {
            result[i] = each.element;
            each = each.next;
            i++;
        }

        return result;
    }
}
