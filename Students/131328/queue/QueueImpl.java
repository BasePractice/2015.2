
import java.util.Iterator;

public final class QueueImpl<E> implements IQueue<E> {

    private int size;
    private Node<E> first;
    private Node<E> last;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void push(E element) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    @Override
    public E pop() {
        final E current = first.item;
        first = first.next;
        if (first == null)
            last = null;
        --size;
        return current;
    }

    @Override
    public E peek() {
        return first.item;
    }

    @Override
    public boolean isEmpty() {
        return size == 0 && first == null;
    }

    @Override
    public void clear() {
        for (Node<E> it = first; it != null; ) {
            Node<E> next = it.next;
            it.next = null;
            it = next;
        }
        first = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl(first);
    }

    private static final class Node<E> {
        final E item;
        Node<E> next;

        private Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }

    private final class IteratorImpl implements Iterator<E> {

        private int nextIndex = 0;
        private Node<E> next;
        private Node<E> returned;

        private IteratorImpl(Node<E> next) {
            this.next = next;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size && next != null;
        }

        @Override
        public E next() {
            returned = next;
            next = next.next;
            nextIndex++;
            return returned.item;
        }
    }
}
