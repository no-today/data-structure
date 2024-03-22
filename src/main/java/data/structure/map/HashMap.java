package data.structure.map;

import data.structure.Map;
import data.structure.Set;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    public HashMap(float threshold, int capacity) {
        this.threshold = threshold;
        this.capacity = capacity;
    }

    public HashMap() {
        this(DEFAULT_THRESHOLD, DEFAULT_CAPACITY);
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
            if (node.key.equals(key)) {
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

    Node<K, V> findNode(int hash, K key) {
        if (table == null) return null;

        Node<K, V> node = table[hash];
        while (node != null) {
            if (Objects.equals(key, node.key)) return node;
            node = node.next;
        }

        return null;
    }

    V getVal(int hash, K key) {
        return Optional.ofNullable(findNode(hash, key)).map(e -> e.val).orElse(null);
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
                return curNode.val;
            }

            prevNode = curNode;
            curNode = curNode.next;
        }

        return null;
    }

    @Override
    public V get(K key) {
        return getVal(hash(key), key);
    }

    @Override
    public boolean containsKey(K key) {
        return findNode(hash(key), key) != null;
    }

    @Override
    public boolean containsValue(V value) {
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
        Set<Entry<K, V>> set = new HashSet<>(1, size);
        foreach(set::add);

        return set;
    }

    @Override
    public Set<K> entryKey() {
        Set<K> set = new HashSet<>(1, size);
        foreach(e -> set.add(e.key));

        return set;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HashMap{");
        foreach(e -> sb.append(e.toString()).append(", "));
        return sb.replace(sb.length() - 2, sb.length(), "").append("}").toString();
    }

    public String toStringK() {
        StringBuilder sb = new StringBuilder("{");
        foreach(e -> sb.append(e.toStringK()).append(", "));
        return sb.replace(sb.length() - 2, sb.length(), "").append("}").toString();
    }

    private void foreach(Consumer<Node<K, V>> consumer) {
        if (table == null) return;
        for (Node<K, V> kvNode : table) {
            if (kvNode == null) continue;
            while (kvNode != null) {
                consumer.accept(kvNode);
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

        public String toStringK() {
            return (key instanceof Number || key instanceof Boolean || key instanceof Map.Entry) ? String.valueOf(key) : "\"" + key + "\"";
        }
    }
}
