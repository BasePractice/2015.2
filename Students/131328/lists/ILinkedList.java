public interface ILinkedList<E> extends Iterable<E> {

    int size();

    boolean isEmpty();

    boolean contains(E element);

    void add(E element);

    void remove(E element);

    void clear();
}
