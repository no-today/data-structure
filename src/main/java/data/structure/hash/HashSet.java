package data.structure.hash;

import data.structure.Set;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author no-today
 * @date 2024/03/22 11:12
 */
public class HashSet<E> implements Set<E> {

    private final HashMap<E, Void> table;

    public HashSet() {
        this.table = new HashMap<>();
    }

    public HashSet(float threshold, int capacity) {
        this.table = new HashMap<>(threshold, capacity);
    }

    @Override
    public boolean add(E element) {
        if (contains(element)) return false;
        table.put(element, null);
        return true;
    }

    @Override
    public boolean remove(E element) {
        return table.removeNode(element) != null;
    }

    @Override
    public boolean contains(Object element) {
        return table.containsKey(element);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public void clear() {
        table.clear();
    }

    @Override
    public E[] toArray(E[] array) {
        if (array.length < size()) {
            array = (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size());
        }

        E[] finalArray = array;
        AtomicInteger i = new AtomicInteger(0);
        table.foreach(e -> finalArray[i.getAndIncrement()] = e.key);
        return finalArray;
    }

    @Override
    public String toString() {
        return "HashSet" + table.toStringK();
    }
}
