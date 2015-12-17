import java.util.Iterator;

public class Test {
    public static void main(String[] args) {

        ILinkedList<Integer> linkedList = new LinkedListImpl<Integer>();
        linkedList.add(1);
        linkedList.add(5);
        linkedList.add(2);
        linkedList.add(3);
        Algorithms.sort(linkedList);
        Iterator<Integer> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());

        }
    }
}
