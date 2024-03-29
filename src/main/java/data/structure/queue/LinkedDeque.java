package data.structure.queue;

import data.structure.Deque;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 双向队列
 *
 * @author no-today
 * @date 2024/03/21 16:40
 */
public class LinkedDeque<E> implements Deque<E> {

    private Node<E> head, tail;
    private int size;

    /**
     * 添加到头部
     */
    public void offerFirst(E e) {
        Node<E> newNode = new Node<>(e);

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            Node<E> oldHead = this.head;
            newNode.next = oldHead;
            oldHead.prev = newNode;

            this.head = newNode;
        }

        size++;
    }

    /**
     * 添加到尾部
     */
    public void offerLast(E e) {
        Node<E> newNode = new Node<>(e);

        if (isEmpty()) {
            this.tail = newNode;
            this.head = newNode;
        } else {
            Node<E> oldTail = this.tail;
            newNode.prev = oldTail;
            oldTail.next = newNode;

            this.tail = newNode;
        }

        size++;
    }


    public E peekFirst() {
        return this.head == null ? null : this.head.element;
    }

    public E peekLast() {
        return this.tail == null ? null : this.tail.element;
    }

    public E pollFirst() {
        if (this.head == null) return null;

        Node<E> oldHead = this.head;
        this.head = this.head.next;

        if (this.head == null) {
            this.tail = null;
        } else {
            this.head.prev = null;
        }

        size--;

        return oldHead.element;
    }

    public E pollLast() {
        if (this.tail == null) return null;

        Node<E> oldTail = this.tail;
        this.tail = oldTail.prev;

        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }

        size--;
        return oldTail.element;
    }

    @Override
    public boolean add(E e) {
        offerLast(e);
        return true;
    }

    @Override
    public boolean remove(E e) {
        Node<E> node = findNode(e);
        if (node == null) return false;
        remove(node);
        return true;
    }

    private E remove(Node<E> delNode) {
        E element = delNode.element;

        if (delNode.prev == null && delNode.next == null) {
            head = tail = null;
        } else if (delNode.prev == null) {
            Node<E> next = delNode.next;
            next.prev = null;
            head = next;
        } else if (delNode.next == null) {
            Node<E> prev = delNode.prev;
            prev.next = null;
            tail = prev;
        } else {
            delNode.prev.next = delNode.next;
            delNode.next.prev = delNode.prev;

            delNode.prev = delNode.next = null;
        }

        size--;
        return element;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object e) {
        return Objects.nonNull(findNode(e));
    }

    private Node<E> findNode(Object e) {
        Node<E> cursor = head;
        while (cursor != null) {
            if (Objects.equals(cursor.element, e)) {
                return cursor;
            }
            cursor = cursor.next;
        }

        return null;
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        Node<E> cursor = this.head;
        int i = 0;
        while (cursor != null) {
            consumer.accept(cursor.element, i++);
            cursor = cursor.next;
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

    static class Node<T> {
        Node<T> prev, next;
        T element;

        public Node(T element) {
            this.element = element;
        }
    }
}
