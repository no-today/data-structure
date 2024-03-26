package data.structure;

/**
 * @author no-today
 * @date 2018/6/26
 */
public interface List<E> extends Collection<E> {

    void addFirst(E element);

    void addLast(E element);

    E remove(int index);

    E removeFirst();

    E removeLast();

    E set(int index, E element);

    E get(int index);

    int indexOf(E element);

    int lastIndexOf(E element);
}
