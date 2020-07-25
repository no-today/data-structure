package data.structure.stack;

import data.structure.Stack;
import data.structure.list.ArrayList;

/**
 * @author no-today
 * @date 2020/3/22 14:42
 */
public class ArrayStack<E> implements Stack<E> {

    private ArrayList<E> arrayList;

    public ArrayStack() {
        this.arrayList = new ArrayList<>();
    }

    public ArrayStack(int size) {
        this.arrayList = new ArrayList<>(size);
    }

    @Override
    public void push(E element) {
        arrayList.add(element);
    }

    @Override
    public E pop() {
        return arrayList.removeLast();
    }

    @Override
    public E peek() {
        return arrayList.get(arrayList.size() - 1);
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    @Override
    public void clear() {
        arrayList.clear();
    }
}
