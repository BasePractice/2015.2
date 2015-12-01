import java.util.Date;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        final int n = 8;
        final int m = 5;
        int[][] matrix = new int[n][m];

        Random rand = new Random(new Date().getTime());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                    int value = rand.nextInt(200);
                    matrix[i][j] = value;
            }
        }

        System.out.println("Original matrix");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                    System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        int[][] matrix2 = Transpose(matrix,n,m);
        System.out.println();
        System.out.println("Transpose matrix");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                    System.out.print(matrix2[i][j] + " ");

            }
            System.out.println();
        }

        int[][] matrix3 = Transpose(matrix2,m,n);
        System.out.println();
        System.out.println("Double-transpose matrix");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix3[i][j] + " ");

            }
            System.out.println();
        }

    }

    public static int[][] Transpose(int[][] matrix, int n, int m) {
        int[][] newmatrix = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newmatrix[j][i] = matrix[i][j];
            }
        }
            return newmatrix;
    }
}
