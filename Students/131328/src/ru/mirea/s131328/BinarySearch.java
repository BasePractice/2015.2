package ru.mirea.s131328;


public class BinarySearch {

    int array[];

    public BinarySearch(int i) {
        array = new int[i];
    }

    public static void setArr(int a[]) {
        for (int k = 0; k < a.length; k++) {
            a[k] = (int) (Math.random() * 10);
        }
        for (int i = a.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    public static void seekingNumber(int a[], int key) {
        int l = 0;
        int mid = -1;
        int h = a.length - 1;
        while (l < h) {
            mid = (l + (h - l) / 2);
            if (a[mid] == key) {
                break;
            } else if (a[mid] > key) {
                h = mid - 1;
            } else if (a[mid] < key) {
                l = mid + 1;
            } else mid = -1;
        }
        System.out.println(mid);
    }

    public static void main(String args[]) {
        int[] object = new int[10];
        setArr(object);
        for (int i = 0; i < object.length; i++) {
            System.out.println(object[i]);
        }
        seekingNumber(object, 10);
    }
}
