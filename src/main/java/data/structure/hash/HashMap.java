package data.structure.hash;

import data.structure.Collection;
import data.structure.List;
import data.structure.Map;
import data.structure.Set;
import data.structure.list.ArrayList;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author no-today
 * @date 2024/03/21 20:55
 */
public class HashMap<K, V> implements Map<K, V> {

    private static final float DEFAULT_THRESHOLD = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;

    private Node<K, V>[] table;

    /**
     * 容量达到 75% 触发扩容
     */
    private final float threshold;

    /**
     * 容量
     */
    private int capacity;

    private int size;

    private int resizeCount;

    public HashMap(float threshold, int capacity) {
        if (threshold <= 0 || threshold > 1) threshold = DEFAULT_THRESHOLD;
        if (capacity < 0) capacity = DEFAULT_CAPACITY;
        this.threshold = threshold;
        this.capacity = capacity;
    }

    public HashMap(int capacity) {
        this(DEFAULT_THRESHOLD, capacity);
    }

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * 当前以使用元素数量的占比
     */
    private double loadFactor() {
        return (double) size / capacity;
    }

    int hash(Object key) {
        int h;
        return (key == null) ? 0 : ((h = key.hashCode()) ^ (h >>> 16)) & (capacity - 1);
    }

    final V putVal(int hash, K key, V val) {
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = new Node<>(hash, key, val, null);
            size++;
            return null;
        }

        while (true) {
            if (Objects.equals(node.key, key)) {
                V oldVal = node.val;
                node.val = val;
                return oldVal;
            }

            // 添加到链尾
            if (node.next == null) {
                node.next = new Node<>(hash, key, val, null);
                size++;
                return null;
            }

            node = node.next;
        }
    }

    Node<K, V> findNode(int hash, Object key) {
        if (table == null) return null;

        Node<K, V> node = table[hash];
        while (node != null) {
            if (Objects.equals(key, node.key)) return node;
            node = node.next;
        }

        return null;
    }

    V getVal(int hash, Object key) {
        Node<K, V> node = findNode(hash, key);
        if (node == null) return null;
        return node.val;
    }

    /**
     * 扩容
     */
    void extend() {
        Node<K, V>[] oldTable = table;

        /*
         * 每次扩容为两倍大小
         */
        size = 0;
        capacity = capacity * 2;
        table = new Node[capacity];

        // 遍历旧的, put 到新的里
        for (Node<K, V> node : oldTable) {
            if (node == null) continue;

            while (node != null) {
                putVal(hash(node.key), node.key, node.val);
                node = node.next;
            }
        }

        resizeCount++;
    }

    @Override
    public V put(K key, V value) {
        if (table == null) {
            table = new Node[capacity];
        }

        // 检查扩容
        if (loadFactor() > threshold) {
            extend();
        }

        return putVal(hash(key), key, value);
    }

    @Override
    public V remove(K key) {
        Entry<K, V> kvEntry = removeNode(key);
        if (kvEntry == null) return null;
        return kvEntry.getValue();
    }

    Entry<K, V> removeNode(K key) {
        if (table == null) return null;

        int hash = hash(key);
        Node<K, V> prevNode = null, curNode = table[hash];
        while (curNode != null) {
            if (Objects.equals(key, curNode.key)) {
                // 删除头节点
                if (prevNode == null) {
                    table[hash] = curNode.next;
                } else {
                    prevNode.next = curNode.next;
                }

                size--;
                return curNode;
            }

            prevNode = curNode;
            curNode = curNode.next;
        }

        return null;
    }

    @Override
    public V get(Object key) {
        return getVal(hash(key), key);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V v = get(key);
        return v == null ? defaultValue : v;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode(hash(key), key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (table == null) return false;

        for (Node<K, V> node : table) {
            if (node == null) continue;
            while (node != null) {
                if (Objects.equals(value, node.val)) return true;
                node = node.next;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        table = null;
        size = 0;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<Entry<K, V>>(1, size);
        foreach((e, i) -> set.add(e));

        return set;
    }

    @Override
    public Set<K> entryKey() {
        Set<K> set = new HashSet<>(1, size);
        foreach((e, i) -> set.add(e.getKey()));

        return set;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>(size);
        foreach((e, i) -> values.add(e.getValue()));
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HashMap{");
        foreach((e, i) -> sb.append(e).append(i == size() - 1 ? "}" : ", "));
        return sb.toString();
    }

    public int getResizeCount() {
        return resizeCount;
    }

    public void foreach(BiConsumer<Entry<K, V>, Integer> consumer) {
        if (table == null) return;

        int i = 0;
        for (Node<K, V> kvNode : table) {
            if (kvNode == null) continue;
            while (kvNode != null) {
                consumer.accept(kvNode, i++);
                kvNode = kvNode.next;
            }
        }
    }

    static class Node<K, V> implements Entry<K, V> {
        final int hash;
        final K key;
        V val;
        Node<K, V> next;

        public Node(int hash, K key, V val, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(val);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HashMap.Node)) return false;

            Node<?, ?> entry = (Node<?, ?>) o;

            if (getKey() != null ? !getKey().equals(entry.getKey()) : entry.getKey() != null) return false;
            return Objects.equals(val, entry.val);
        }

        @Override
        public String toString() {
            String valStr = (val instanceof Number || val instanceof Boolean) ? String.valueOf(val) : "\"" + val + "\"";
            return String.format("\"%s\": %s", key, valStr);
        }
    }
}
