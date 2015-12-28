
import java.util.*;

public final class Algorithms {
    public static <E> void sort(ILinkedList<E> list) {
        LinkedList<E> sublist = new LinkedList<>();
        for (E aList : list) {
            sublist.add(aList);
        }
        E[] arr = (E[]) sublist.toArray();
        Arrays.sort(arr);
        list.clear();
        for (E anArr : arr) {
            list.add(anArr);
        }
    }

    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset) {
        ILinkedList<E> result = new LinkedListImpl<>();
        if (list instanceof LinkedListImpl) {
            LinkedListImpl<E> impl = (LinkedListImpl<E>) list;
            LinkedListImpl.Node<E> node = impl.indexOf(offset);
            if (node == null)
                return result;
            for (LinkedListImpl.Node<E> next = node; next != null; next = next.next) {
                result.add(next.item);
            }
            return result;
        }
        return list;
    }

    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset, int size) {
        ILinkedList<E> result = new LinkedListImpl<>();
        if (list instanceof LinkedListImpl) {
            LinkedListImpl<E> impl = (LinkedListImpl<E>) list;
            LinkedListImpl.Node<E> node = impl.indexOf(offset);
            if (node == null)
                return result;
            for (LinkedListImpl.Node<E> next = node; result.size() != size && next != null; next = next.next) {
                result.add(next.item);
            }
            return result;
        }
        return list;
    }

    public static <E> ILinkedList<E> addList(ILinkedList<E> list, ILinkedList<E> other) {
        for (E elem : other)
            list.add(elem);
        return list;
    }

    public static <E> ILinkedList<E> removeList(ILinkedList<E> list, E offset, int size) {
        Iterator<E> iterator = list.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if (obj.equals(offset) || (count != 0 && count < size)) {
                list.remove(obj);
                count++;
            }
        }
        return list;
    }

    public static <E> boolean compareList(ILinkedList<E> list, ILinkedList<E> other) {
        if (list.size() != other.size())
            return false;
        Iterator<E> iteratorList = list.iterator();
        Iterator<E> iteratorOther = other.iterator();
        while (iteratorList.hasNext()) {
            if (!(iteratorList.next().equals(iteratorOther.next())))
                return false;
        }

        return true;
    }

    public interface LinkedListFold<R, E> {
        R execute(R accum, E element);
    }

    public static <E extends Comparable<E>, R> R foldl(ILinkedList<E> list, R accum, LinkedListFold<R, E> func) {
        for (E element : list) {
            accum = func.execute(accum, element);
        }
        return accum;
    }
}
