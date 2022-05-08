package data.structure;

/**
 * @author no-today
 * @date 2022/05/07 23:26
 */
public interface Tree<E extends Comparable<E>> {

    void add(E element);

    boolean contains(E element);

    int size();

    boolean isEmpty();
}
