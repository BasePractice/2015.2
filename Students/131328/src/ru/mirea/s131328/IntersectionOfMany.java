package ru.mirea.s131328;

import java.util.ArrayList;
import java.util.List;


public class IntersectionOfMany {
    public static void main(String[] args) {


        int[] array1 = {0, 1, 2, 3, 4, 5, 8};
        int[] array2 = {1, 2, 3, 4, 5, 12};
        List<Integer> result = new ArrayList();
        int i = 0;
        int j = 0;
        int n = array1.length;
        int m = array2.length;

        while (i < n && j < m) {
            if (array1[i] == array2[j]) {
                result.add(array1[i]);
                i++;
                j++;
            } else {
                if (array1[i] < array2[j]) {
                    i++;
                } else {
                    j++;
                }
            }
        }
        for (int a : result) {
            System.out.println(a);
        }
    }

}
