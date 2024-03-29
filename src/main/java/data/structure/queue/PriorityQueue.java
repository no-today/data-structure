package data.structure.queue;

import data.structure.Queue;
import data.structure.list.ArrayList;

import java.util.function.BiConsumer;

/**
 * 优先队列 二叉堆实现
 * <p>
 * parent(i) = i/2
 * left child (i) = 2 * i + 1
 * right child (i) = 2 * i + 2
 */
public class PriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private final ArrayList<E> array;

    public PriorityQueue() {
        this.array = new ArrayList<>();
    }

    @Override
    public void enqueue(E element) {
        array.addLast(element);

        siftUp(array.size() - 1);
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }

        E e = array.get(0);
        array.swap(0, array.size() - 1);
        siftDown(0);

        return e;
    }

    @Override
    public E front() {
        if (isEmpty()) {
            return null;
        }

        return array.get(0);
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean contains(Object e) {
        return array.contains(e);
    }

    @Override
    public void foreach(BiConsumer<E, Integer> consumer) {
        array.foreach(consumer);
    }

    @Override
    public E[] toArray(E[] arr) {
        return array.toArray(arr);
    }

    @Override
    public boolean add(E e) {
        enqueue(e);
        return true;
    }

    @Override
    public boolean remove(E e) {
        return array.remove(e);
    }

    @Override
    public void clear() {
        array.clear();
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        return i * 2 + 1;
    }

    private int rightChild(int i) {
        return i * 2 + 2;
    }

    /**
     * 上浮
     * <p>
     * 子节点比父节点大, 入队时执行
     */
    private void siftUp(int i) {
        // 终止条件: 子节点比父节点小 \ 没有父节点了: parent(i) == 0
        while (i > 0 && array.get(parent(i)).compareTo(array.get(i)) < 0) {
            array.swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * 下沉
     *
     * <p>
     * 出队时执行
     */
    private void siftDown(int i) {
        // 终止条件: 后继无人, 左边都没有, 右边肯定也没有
        while (leftChild(i) > array.size()) {
            int leftIndex = leftChild(i);

            // 存在右节点并且比左节点大, 使用右节点
            if (leftIndex + 1 < array.size() && array.get(leftIndex + 1).compareTo(array.get(leftIndex)) > 0) {
                array.swap(i, leftIndex + 1);

                i = leftIndex + 1;
            } else {
                array.swap(i, leftIndex);

                i = leftIndex;
            }
        }
    }
}
