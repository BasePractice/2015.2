
public class TestSet {
    public static void main(String[] args) {
        ISet<Integer> set1 = new SetImpl<>();
        ISet<Integer> set2 = new SetImpl<>();
        ISet<Integer> set3 = new SetImpl<>();
        set2.put(1);
        set2.put(2);
        set2.put(3);
        set2.put(4);
        set2.put(5);
        set1.put(4);
        set1.put(5);
        set1.put(6);
        set1.put(7);
        set1.put(8);
        set3 = AlgorithmsSet.difference(set1, set2);
        print(set3);
        System.out.println(AlgorithmsSet.isIntersection(set1, set2));
    }


    public static <E> void print(ISet<E> set) {
        for (E e : set) {
            System.out.print(e.toString() + " ");

        }
        System.out.println("");
        System.out.println("---------------------------------------------------------------");
    }
}

