
public interface IQueue<E> extends Iterable<E> {
    int size();

    void push(E element);

    E pop();

    E peek();

    boolean isEmpty();

    void clear();
}
