package data.structure.skiplist;

/**
 * 排序的链表
 *
 * @author no-today
 * @date 2022/04/30 21:43
 */
public class SortedList<E extends Comparable<E>> {

    private Node<E> head;

    /**
     * O(N)
     */
    public void add(E element) {
        // 首次插入头节点
        if (this.head == null) {
            this.head = new Node<>(element);
            return;
        }

        /*
         * list:   1 4 7 9
         * insert: 5
         *
         * 找到第一个比 5 大的的前一个节点
         * 插在该节点的右边
         */
        Node<E> node = this.head;
        while (node.right != null && element.compareTo(node.right.element) >= 0) {
            node = node.right;
        }

        Node<E> newNode = new Node<>(element);
        if (element.compareTo(node.element) < 0) {
            // 正常都应该插入在已有节点右侧
            // 如果插在左侧, 说明插入的元素比已有元素都要小, 需要更新 head 节点为新节点
            newNode.right = node; // node == head
            this.head = newNode;
        } else {
            // 插在右边
            newNode.right = node.right;
            node.right = newNode;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        Node<E> node = head;
        while (node != null) {
            if (node.right == null) {
                sb.append(node.element);
            } else {
                sb.append(node.element).append(", ");
            }

            node = node.right;
        }

        sb.append("]");

        return sb.toString();
    }

    static class Node<E> {
        E element;
        Node<E> right;

        public Node(E element) {
            this.element = element;
        }
    }
}
