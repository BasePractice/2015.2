package ru.mirea.oop.practice.set;

public final class Algorithms {
    /** Вхождение */
    public static <E> boolean isIncluded(ISet<E> set, ISet<E> include) {
        boolean f = true;
        for (E e : set) {
            boolean f1 = false;
            for (E e1 : include) {
                if (e.equals(e1)) {f1 = true;}
            }
            if (!f1) {f = false;}
        }
        return f;
    }
    /** Равенство */
    public static <E> boolean isEquals(ISet<E> set, ISet<E> other) {
        return set.equals(other);
    }
    /** Пересечение */
    public static <E> boolean isIntersection(ISet<E> set, ISet<E> other) {
        boolean f = false;
        for (E e : set) {
            for (E e1 : other) {
                if (e.equals(e1)) {f = true;}
            }
        }
        return f;
    }
    /** Объединение */
    public static <E> ISet<E> union(ISet<E> set, ISet<E> other) {
        SetImpl<E> result = new SetImpl<E>();
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
        SetImpl<E> result = new SetImpl<E>();
        for (E e : set) {
            for (E e1 : other) {
                if (e.equals(e1)) {result.put(e);}
            }
        }
        return result;
    }
    /** Разность */
    public static <E> ISet<E> difference(ISet<E> set, ISet<E> other) {
        SetImpl<E> result = new SetImpl<E>();
        for (E e : set) {
            boolean f1 = false;
            for (E e1 : other) {
                if (e.equals(e1)) {f1 = true;}
            }
            if (!f1) {result.put(e);}
        }
        return result;
    }
    /** Произведение */
    public static <E> ISet<E> composition(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }
}
