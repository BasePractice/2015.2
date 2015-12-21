package ru.mirea.s131328;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Транспонированная матрица
 */
public class TransposedMatrix {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String str = reader.readLine();
        int n = Integer.parseInt(str);
        int[][] a = new int[n][n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = Math.abs(rand.nextInt() % 100);
                System.out.print(a[i][j] + " ");
            }
            System.out.println(" ");
        }

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = a[j][i];
                a[j][i] = a[i][j];
                a[i][j] = temp;
            }
        }

        System.out.println("Транспонированная матрица:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println(" ");
        }

    }

}
