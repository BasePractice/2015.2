package ru.mirea.s131328;

/**
 * сложение матриц
 */
public class MatrixAddition {
    public static void main(String[] args) {
        int n = 3;
        int m = 3;
        int k = 3;

        int[][] matr1 = new int[n][];
        int[][] matr2 = new int[m][];
        int[][] result = new int[n][];

        for (int i = 0; i < result.length; i++)
            result[i] = new int[k];

        System.out.println("Matrix 1:");
        for (int i = 0; i < matr1.length; i++) {
            matr1[i] = new int[m];
            for (int j = 0; j < matr1[i].length; j++) {
                matr1[i][j] = (int) Math.round(Math.random() * 10);
                System.out.print(matr1[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println("Matrix 2:");
        for (int i = 0; i < matr2.length; i++) {
            matr2[i] = new int[k];
            for (int j = 0; j < matr2[i].length; j++) {
                matr2[i][j] = (int) Math.round(Math.random() * 10);
                System.out.print(matr2[i][j] + "   ");
            }
            System.out.println();
        }

        for (int i = 0; i < matr1.length; i++) { //сложение
            for (int j = 0; j < matr2[i].length; j++) {
                result[i][j] = matr1[i][j] + matr2[i][j];
            }
        }

        System.out.println("Result:");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + "   ");
            }
            System.out.println();
        }
    }
}
