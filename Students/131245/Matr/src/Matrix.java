package Matr.src;

/**
 * Created by aleksejpluhin on 01.12.15.
 */
public class Matrix {
    public static void main(String[] args) {
        int[][] map = {
                {1, 2, 3, 4},
                {1, 2, 3, 4},
                {1, 2, 3, 4},

        };
        printMat(transp(map));
        printMat(transp(transp(map)));

    }

    public static int[][] transp(int[][] matrix) {

        int[][] mat1 = new int[matrix[0].length][matrix.length];
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(i == j ) {
                    mat1[i][j] = matrix[i][j];
                } else {
                    mat1[j][i] = matrix[i][j];
                }
            }
        }
        return mat1;
    }


    public static void printMat(int[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
