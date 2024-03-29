package data.structure.hash;

import data.structure.Map;
import data.structure.Set;

import java.util.function.BiConsumer;

/**
 * @author no-today
 * @date 2024/03/22 11:12
 */
public class HashSet<E> implements Set<E> {

    private static final Object PRESENT = new Object();
    private final HashMap<E, Object> table;

    public HashSet() {
        this.table = new HashMap<>();
    }

    public HashSet(float threshold, int capacity) {
        this.table = new HashMap<>(threshold, capacity);
    }

    @Override
    public boolean add(E element) {
        return table.put(element, PRESENT) == null;
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
    public void foreach(BiConsumer<E, Integer> consumer) {
        table.foreach((e, i) -> consumer.accept(e.getKey(), i));
    }

    @Override
    public E[] toArray(E[] arr) {
        if (arr.length < size()) {
            arr = (E[]) java.lang.reflect.Array.newInstance(arr.getClass().getComponentType(), size());
        }

        E[] finalArr = arr;
        table.foreach((e, i) -> finalArr[i] = e.getKey());
        return finalArr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HashSet{");
        foreach((e, i) -> sb.append((e instanceof Number || e instanceof Boolean || e instanceof Map.Entry) ? String.valueOf(e) : "\"" + e + "\"").append(", "));
        return sb.replace(sb.length() - 2, sb.length(), "").append("}").toString();
    }
}
