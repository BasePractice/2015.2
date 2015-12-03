package ru.mirea.s131328;

import java.util.Arrays;

public final class BinarySearch {

    public static void main(String[] args) {
	// write your code here найти индекс элемента методом бисекции.
        //элементы существуют
        //элементы не существуют = -1
        //массив равен 0
     //riseindexof
        Object[] array = new Object[] {5, 10, 2};
        Arrays.sort(array);
        int index = Arrays.asList(array).indexOf(5);
        System.out.println("index = " + index);
    }
}
