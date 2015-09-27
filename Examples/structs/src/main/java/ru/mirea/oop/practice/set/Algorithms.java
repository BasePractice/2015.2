package ru.mirea.oop.practice.set;

public final class Algorithms {
    /** Вхождение */
    public static <E> boolean isIncluded(ISet<E> set, ISet<E> include) {
        for (E e : include) {
            if (!set.contains(e)) {
                return false;
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
        for (E e : set) {
            if (other.contains(e)) {
                return true;
            }
        }
        return false;
    }
    /** Объединение */
    public static <E> ISet<E> union(ISet<E> set, ISet<E> other) {
        ISet<E> result = new SetImpl<>();
        for (E e : set) {
            result.put(e);
        }
        for (E e : other) {
            result.put(e);
        }
        return result;
    }
    /** Пересечение */
    public static <E> ISet<E> intersection(ISet<E> set, ISet<E> other) {
        ISet<E> result = new SetImpl<>();
        for (E e : set) {
            if (other.contains(e)) {
                result.put(e);
            }
        }
        return result;

    }
    /** Разность */
    public static <E> ISet<E> difference(ISet<E> set, ISet<E> other) {
        ISet<E> result = new SetImpl<>();
        for (E e : union(set, other)) {
            if (!set.contains(e) || !other.contains(e)) {
                result.put(e);
            }
        }
        return result;
    }
    /** Произведение */
    public static <E> ISet<E> composition(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }
}
