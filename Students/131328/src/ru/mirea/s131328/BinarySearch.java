package ru.mirea.s131328;

import java.util.Arrays;

public class BinarySearch {

    private BinarySearch() {
    }

    public static int indexOf(int[] a, int key) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    public static void main(String[] args) {

        // initializing unsorted int array
        int intArr[] = {31, 10, 22, 14, 56};

        // sorting array
        Arrays.sort(intArr);

        // let us print all the elements available in list
        System.out.println("The sorted int array is:");
        for (int number : intArr) {
            System.out.println("Number = " + number);
        }

        // entering the value to be searched
        int searchVal = 31;

        int retVal = BinarySearch.indexOf(intArr, searchVal);

        System.out.println("The index of element 31 is : " + retVal);
    }
}



