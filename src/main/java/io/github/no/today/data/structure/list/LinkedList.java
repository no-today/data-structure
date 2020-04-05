package io.github.no.today.data.structure.list;

import io.github.no.today.data.structure.List;

/**
 * @author no-today
 * @date 2018/6/26
 */
public class LinkedList<E> implements List<E> {

    /**
     * Head node
     */
    private Node<E> head;

    /**
     * Tail node
     */
    private Node<E> tail;

    /**
     * length
     */
    private int length;

    /**
     * ToArray use
     */
    private int index;

    @Override
    public void add(E e) {
        Node<E> lastTemp = tail;

        /*
         * 空链的情况
         * 	首尾都指向新节点
         *
         * 其他情况
         * 	尾节点指向新节点
         */
        if (lastTemp == null) {
            Node<E> newNode = new Node<>(null, e, null);
            tail = newNode;
            head = newNode;
        } else {
            Node<E> newNode = new Node<>(lastTemp, e, null);
            lastTemp.next = newNode;
            tail = newNode;
        }
        length++;
    }

    @Override
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(null, e, head);

        if (head != null) {
            head.pre = newNode;
        } else {
            tail = newNode;
        }

        length++;
        head = newNode;
    }

    @Override
    public void addLast(E e) {
        Node<E> newNode = new Node<>(tail, e, null);

        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }

        length++;
        tail = newNode;
    }

    @Override
    public E remove(int index) {
        checkIndexOutBounds(index);

        Node<E> firstTemp = head;
        for (int i = 0; i < index; i++) {
            firstTemp = firstTemp.next;
        }

        return remove(firstTemp);
    }

    @Override
    public boolean remove(E e) {
        boolean flag = false;
        Node<E> firstTemp = head;

        while (firstTemp != null) {
            if (firstTemp.element.equals(e)) {
                remove(firstTemp);
                flag = true;
            }
            firstTemp = firstTemp.next;
        }

        return flag;
    }

    @Override
    public E removeFirst() {
        if (head == null) {
            return null;
        }

        E result = head.element;
        head.element = null;

        Node<E> next = head.next;
        if (next != null) {
            next.pre = null;
        } else {
            tail = null;
        }

        head = next;

        length--;
        return result;
    }

    @Override
    public E removeLast() {
        if (tail == null) {
            return null;
        }

        E result = tail.element;
        tail.element = null;

        Node<E> pre = tail.pre;
        if (pre != null) {
            pre.next = null;
        } else {
            head = null;
        }

        tail = pre;

        length--;
        return result;
    }

    @Override
    public E set(int index, E element) {
        checkIndexOutBounds(index);

        Node<E> firstTemp = head;
        for (int i = 0; i < index; i++) {
            firstTemp = firstTemp.next;
        }

        firstTemp.element = element;

        return element;
    }

    @Override
    public E get(int index) {
        checkIndexOutBounds(index);

        Node<E> firstTemp = head;
        for (int i = 0; i < index; i++) {
            firstTemp = firstTemp.next;
        }

        return firstTemp.element;
    }

    /**
     * O(n)
     *
     * @param o
     * @return
     */
    @Override
    public boolean contains(E o) {
        Node<E> firstTemp = head;

        while (firstTemp != null) {
            if (firstTemp.element.equals(o)) {
                return true;
            }
            firstTemp = firstTemp.next;
        }
        return false;
    }

    @Override
    public int indexOf(E e) {
        Node<E> firstTemp = head;

        int index = 0;
        while (firstTemp != null) {
            if (firstTemp.element.equals(e)) {
                return index;
            }
            firstTemp = firstTemp.next;
            index++;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(E e) {
        Node<E> lastTemp = tail;

        int index = length;
        while (lastTemp != null) {
            if (lastTemp.element.equals(e)) {
                return index;
            }
            lastTemp = lastTemp.pre;
            index--;
        }

        return -1;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * O(n)
     */
    @Override
    public void clear() {
        Node<E> firstTemp = head;

        while (firstTemp != null) {
            firstTemp.element = null;
            firstTemp.pre = null;

            Node<E> nextTemp = firstTemp.next;
            firstTemp.next = null;
            firstTemp = nextTemp;
        }
        head = null;
        tail = null;
        length = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] results = new Object[length];
        toArray(head, results);
        index = 0;
        return results;
    }

    @Override
    public E[] toArray(E[] array) {
        if (array == null || array.length < length) {
            array = (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), length);
        }

        E[] results = array;
        toArray_(head, results);
        index = 0;
        return results;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");

        Node<E> current = head;
        while (current != null) {
            if (current.next == null) {
                sb.append(current.element.toString());
            } else {
                sb.append(current.element.toString()).append(", ");
            }
            current = current.next;
        }

        sb.append('}');
        return sb.toString();
    }

    private void toArray(Node<E> node, Object[] array) {
        if (node == null) {
            return;
        }

        array[index++] = node.element;
        toArray(node.next, array);
    }

    private void toArray_(Node<E> node, E[] array) {
        if (node == null) {
            return;
        }

        array[index++] = node.element;
        toArray(node.next, array);
    }

    private E remove(Node<E> delNode) {
        E result = delNode.element;
        delNode.element = null;

        /*
         * 单节点
         * 头节点
         * 尾节点
         * 中节点
         */
        if (delNode.pre == null && delNode.next == null) {
            /*
             * 置空前后索引
             */
            head = tail = null;
            delNode.next = null;
            delNode.pre = null;
        } else if (delNode.pre == null) {
            /*
             * 头节点前移
             * 置空next节点的pre指向
             */
            Node<E> next = delNode.next;
            next.pre = null;
            head = next;
        } else if (delNode.next == null) {
            /*
             * 尾节点后移
             * 置空pre节点的next指向
             */
            Node<E> pre = delNode.pre;
            pre.next = null;
            tail = pre;
        } else {
            delNode.pre.next = delNode.next;
            delNode.next.pre = delNode.pre;
            delNode.next = null;
            delNode.pre = null;
        }

        length--;
        return result;
    }

    private void checkIndexOutBounds(int index) {
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Node
     *
     * @param <E>
     */
    private static class Node<E> {
        Node<E> pre;
        E element;
        Node<E> next;

        private Node(Node<E> pre, E element, Node<E> next) {
            this.pre = pre;
            this.element = element;
            this.next = next;
        }
    }
}
