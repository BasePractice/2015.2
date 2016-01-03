import java.util.Iterator;

public final class AlgorithmsSet {
    /** Вхождение */
    public static <E> boolean isIncluded(ISet<E> set, ISet<E> include) {
        if(set.size() < include.size()){
            return false;
        } else {
            Iterator<E> iteratorInclude = include.iterator();
            while (iteratorInclude.hasNext()){
                if(!set.contains(iteratorInclude.next())){
                    return false;
                }
            }

        }
        return true;
    }
    /** Равенство */
    public static <E> boolean isEquals(ISet<E> set, ISet<E> other) {
        return set.equals(other);
    }
    /** Пересечение */
    public static <E> boolean isIntersection(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }
    /** Объединение */
    public static <E> ISet<E> union(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }
    /** Пересечение */
    public static <E> ISet<E> intersection(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }
    /** Разность */
    public static <E> ISet<E> difference(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }
    /** Произведение */
    public static <E> ISet<E> composition(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }
}


