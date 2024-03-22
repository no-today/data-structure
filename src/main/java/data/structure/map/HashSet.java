package data.structure.map;

import data.structure.Set;

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
    public void add(E element) {
        table.put(element, null);
    }

    @Override
    public void remove(E element) {
        table.remove(element);
    }

    @Override
    public boolean contains(E element) {
        return table.containsKey(element);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    @Override
    public void clear() {
        table.clear();
    }

    @Override
    public String toString() {
        return "HashSet" + table.toStringK();
    }
}
