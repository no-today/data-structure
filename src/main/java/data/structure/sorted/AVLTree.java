package data.structure.sorted;

/**
 * 平衡二叉树
 * 相比二叉搜索树仅多了添加、删除后的平衡(balance)动作, 这里用继承方式会更清晰
 *
 * @author no-today
 * @date 2024/03/27 09:32
 */
public class AVLTree<E extends Comparable<E>> extends BSTree<E> {

    Node<E> balance(Node<E> node) {
        int balanceFactor = balanceFactor(node);

        /*
         * 失衡节点的平衡因子     子节点的平衡因子     形态     应采用的旋转方法
         * > 1 (左偏树)         >= 0               LL      右旋
         * > 1 (左偏树)         < 0                LR      先左旋左子节点 再右旋
         * < -1 (右偏树)        <= 0               RR      左旋
         * < -1 (右偏树)        > 0                RL      先右旋右子节点 再左旋
         */
        if (balanceFactor > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = leftRotate(node.left);
            }
            return rightRotate(node);
        }

        // 右偏
        if (balanceFactor < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rightRotate(node.right);
            }
            return leftRotate(node);
        }

        return node;
    }

    private Node<E> rightRotate(Node<E> node) {
        /*
         *              10 <- unbalanced          5
         *             /  \                      / \
         *   child -> 5    ?                    0   10
         *           / \
         * added -> 0   ?
         */
        Node<E> child = node.left;

        // 需要继续维持在二者之间的节点
        node.left = child.right;
        child.right = node;

        node.refreshHeight();
        child.refreshHeight();

        return child;
    }

    private Node<E> leftRotate(Node<E> node) {
        /*
         *   0 <- unbalanced           5
         *  / \                       / \
         * ?   5 <- child            0   10
         *    / \
         *   ?   10 <- added
         */
        Node<E> child = node.right;

        // 需要继续维持在二者之间的节点
        node.right = child.left;
        child.left = node;

        node.refreshHeight();
        child.refreshHeight();

        return child;
    }

    @Override
    Node<E> insert(Node<E> node, E e) {
        Node<E> eNode = super.insert(node, e);
        return balance(eNode);
    }

    @Override
    Node<E> delete(Node<E> node, E e) {
        Node<E> eNode = super.delete(node, e);
        return balance(eNode);
    }

    @Override
    Node<E> deleteMin(Node<E> node, E[] result) {
        Node<E> eNode = super.deleteMin(node, result);
        return balance(eNode);
    }

    @Override
    Node<E> deleteMax(Node<E> node, E[] result) {
        Node<E> eNode = super.deleteMax(node, result);
        return balance(eNode);
    }
}
