import java.util.Iterator;

public class Test {
    public static void main(String[] args) {

        ILinkedList<Integer> linkedList = new LinkedListImpl<Integer>();
        ILinkedList<Integer> linkedList1 = new LinkedListImpl<Integer>();
        linkedList.add(1);
        linkedList.add(5);
        linkedList.add(2);
        linkedList.add(3);
        linkedList1.add(1);
        linkedList1.add(7);
        linkedList1.add(2);
        linkedList1.add(3);
        System.out.println(Algorithms.compareList(linkedList,linkedList1));
        Algorithms.sort(linkedList);
        Iterator<Integer> iterator = linkedList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());

        }

        /*
        ILinkedList<String> strings = new LinkedListImpl<>();
        strings.add("lol");
        strings.add("jhg");
        strings.add("dsa");
        strings.add("rgt");
        for (String str : Algorithms.subList(strings, "dsa")){
            System.out.println(str);
        }
         */
    }
}
