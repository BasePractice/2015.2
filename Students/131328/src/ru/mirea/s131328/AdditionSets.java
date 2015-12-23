package ru.mirea.s131328;

import java.util.Set;
import java.util.TreeSet;
/**
 * Сложение множеств
*/
public class AdditionSets {
    public static void main(String[] args) {
        int[] array1 = {0, 1, 2, 3, 4, 5, 8, 999};
        int[] array2 = {1, 2, 3, 4, 5, 12};
        Set<Integer> result = new TreeSet<>();

        for (int a : array1) {
            result.add(a);
        }
        for (int a : array2) {
            result.add(a);
        }


        for (int a : result) {
            System.out.println(a);
        }
    }
}
