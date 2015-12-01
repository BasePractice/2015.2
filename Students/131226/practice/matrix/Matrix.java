package practice.matrix;

import java.util.*;

public class Matrix {
    int height;
    int width;
    List<List<Object>> rows;

    public Matrix() {
    }

    public Matrix(int height, int width) {
        this.height = height;
        this.width = width;
        this.rows = new LinkedList<List<Object>>();
        Random random = new Random((new Date().getTime()));
        for (int i = 0; i < height; i++) {
            List<Object> row = new ArrayList<Object>();
            for (int j = 0; j < width; j++) {
                row.add(random.nextInt(10));
            }
            this.rows.add(row);
        }
    }

    @Override
    public String toString() {
        String matrix = "";
        for (List<Object> row : this.rows) {
            for (Object o : row) {
                matrix+=o.toString() + " ";
            }
            matrix += "\n";

        }
        return matrix;
    }
    public Matrix transpose() {
        Matrix matrixT = new Matrix();
        matrixT.height = this.width;
        matrixT.width = this.height;
        matrixT.rows = new ArrayList<List<Object>>();
        for (int i = 0; i < this.width; i++) {
            List<Object> row = new ArrayList<Object>();
            for (int j = 0; j < this.height; j++) {
                row.add(this.rows.get(j).get(i));
            }
            matrixT.rows.add(row);
        }
        return matrixT;
    }
}
