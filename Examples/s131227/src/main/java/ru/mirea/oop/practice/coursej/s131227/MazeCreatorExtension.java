package ru.mirea.oop.practice.coursej.s131227;

import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

import java.util.Random;

public final class MazeCreatorExtension extends AbstractMazeExtension {

    Random rnd = new Random();
    int rows, cols;

    @Override
    public String description() {
        return "Работа с лабиринтом при помощи алгоритма Hunt And Kill";
    }

    @Override
    public String name() {
        return "maze.services.HuntAndKill";
    }

    @Override
    public Maze generateMaze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        Maze maze = new Maze(rows, cols);

        int f = 0;
        int i, j, p, v;

        //Генерируем лабиринт
        Cells cells = new Cells(rows, cols);
        cells.fillMaze();
        int a = rnd.nextInt(rows);
        int b = rnd.nextInt(cols);
        cells.setGStart(a, b);
        cells.kill(a, b);
        while (f != rows * cols) {
            cells.hunt();
            f = 0;
            for (i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    if (cells.getWalls(i, j) != 0) {
                        f += 1;
                    }
                }
            }
        }
        cells.setExit();

        //Ищем выход из лабиринта
        cells.findExit();

        //Преобразуем к нужному формату
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                maze.data[x][y] = cells.getWalls(x, y);
            }
        }

        //Создаем массив точек пути от начала к выходу
        p = 0;
        for(i = 0; i < rows; i++){
            for(j = 0; j < cols; j++){
                if(cells.getWay(i, j) != 0){
                    p++;
                }
            }
        }
        p++;
        v = 0;
        Point[] path = new Point[p];
        for(i = 0; i < rows; i++){
            for(j = 0; j < cols; j++){
                if(cells.getWay(i, j) != 0){
                    path[v] = new Point(i, j);
                    v++;
                }
            }
        }
        v++;
        path[v] = new Point(9, 9);

        return maze;
    }

    @Override
    public Point[] findPath(Maze maze) {
        return new Point[0];
    }

    class Cells {
        int rows, cols;
        char array[][][];
        int i, j;
        int a, b, c, d, w;
        char v;
        boolean PathFound = false;

        public Cells(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.array = new char[rows][cols][4];
        }

        public void fillMaze() {
            for (i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    this.array[i][j][0] = 0;
                }
            }
            for (i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    this.array[i][j][1] = 0;
                }
            }
        }

        public void hunt() {
            for (i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    if ((i != (rows - 1) && array[i][j][0] == 0 && array[i + 1][j][0] != 0) ||
                            (j != 0 && array[i][j - 1][0] != 0 && array[i][j][0] == 0) ||
                            (j != (cols - 1) && array[i][j][0] == 0 && array[i][j + 1][0] != 0) ||
                            (i != 0 && array[i - 1][j][0] != 0 && array[i][j][0] == 0)) {
                        if (i != (rows - 1) && array[i][j][0] == 0 && array[i + 1][j][0] != 0) {
                            array[i][j][0] = 7;
                            array[i + 1][j][0] -= 2;
                        }
                        if (j != 0 && array[i][j - 1][0] != 0 && array[i][j][0] == 0) {
                            array[i][j][0] = 14;
                            array[i][j - 1][0] -= 4;
                        }
                        if (j != (cols - 1) && array[i][j][0] == 0 && array[i][j + 1][0] != 0) {
                            array[i][j][0] = 11;
                            array[i][j + 1][0] -= 1;
                        }
                        if (i != 0 && array[i - 1][j][0] != 0 && array[i][j][0] == 0) {
                            array[i][j][0] = 13;
                            array[i - 1][j][0] -= 8;
                        }
                        a = i;
                        b = j;
                        this.kill(a, b);
                    }
                }
            }
        }

        public void kill(int a, int b) {
            this.a = a;
            this.b = b;
            while (((a != 0 && array[a - 1][b][0] == 0) || (a != (rows - 1) && array[a + 1][b][0] == 0) ||
                    (b != 0 && array[a][b - 1][0] == 0) || (b != (cols - 1) && array[a][b + 1][0] == 0))) {
                c = (rnd.nextInt(3) - 1);
                d = (rnd.nextInt(3) - 1);
                if (c == -1 && d == 0 && (a != 0 && array[a + c][b + d][0] == 0)) {
                    array[a][b][0] -= 2;
                    a -= 1;
                    array[a][b][0] = 7;
                }
                if (c == 1 && d == 0 && (a != (rows - 1) && array[a + c][b + d][0] == 0)) {
                    array[a][b][0] -= 8;
                    a += 1;
                    array[a][b][0] = 13;
                }
                if (c == 0 && d == -1 && (b != 0 && array[a + c][b + d][0] == 0)) {
                    array[a][b][0] -= 1;
                    b -= 1;
                    array[a][b][0] = 14;
                }
                if (c == 0 && d == 1 && (b != (cols - 1) && array[a + c][b + d][0] == 0)) {
                    array[a][b][0] -= 4;
                    b += 1;
                    array[a][b][0] = 11;
                }
            }
        }

        public char getWalls(int a, int b) {
            return array[a][b][0];
        }

        public char getWay(int a, int b) {
            return array[a][b][3];
        }

        public void setGStart(int a, int b) {
            this.array[a][b][0] = 15;
        }

        public void setExit() {
            array[0][0][0] -= 1;

        }

        public void findExit() {
            w = 2;
            this.array[rows - 1][cols - 1][1] = 1;
            this.array[rows - 1][cols - 1][2] = 1;
            while (PathFound != true) {
                for (i = 0; i < rows; i++) {
                    for (j = 0; j < cols; j++) {
                        if (array[0][0][1] != 0) {
                            PathFound = true;
                        }
                        if (i != (rows - 1) && (array[i + 1][j][0] & 2) != 2 && array[i + 1][j][1] == (w - 1) &&
                                array[i][j][2] != 1) {
                            array[i][j][1] += w;
                            array[i][j][2] = 1;
                        }
                        if (j != 0 && (array[i][j - 1][0] & 4) != 4 && array[i][j - 1][1] == (w - 1) &&
                                array[i][j][2] != 1) {
                            array[i][j][1] += w;
                            array[i][j][2] = 1;
                        }
                        if (j != (cols - 1) && (array[i][j + 1][0] & 1) != 1 && array[i][j + 1][1] == (w - 1) &&
                                array[i][j][2] != 1) {
                            array[i][j][1] += w;
                            array[i][j][2] = 1;
                        }
                        if (i != 0 && (array[i - 1][j][0] & 8) != 8 && array[i - 1][j][1] == (w - 1) &&
                                array[i][j][2] != 1) {
                            array[i][j][1] += w;
                            array[i][j][2] = 1;
                        }
                    }
                }
                w++;
            }
            this.findWay();
        }

        public void findWay() {
            a = 0;
            b = 0;
            array[0][0][3] = array[0][0][1];
            v = array[0][0][1];
            while(v != 1){
                if (a != 0 && (array[a - 1][b][0] & 8) != 8 && array[a - 1][b][1] == (v - 1)) {
                    v--;
                    array[a][b][3] |= v;
                    a--;
                    continue;
                }
                if (a != (rows - 1) && (array[a + 1][b][0] & 2) != 2 && array[a + 1][b][1] == (v - 1)) {
                    v--;
                    array[a][b][3] = v;
                    a++;
                    continue;

                }
                if (b != 0 && (array[a][b - 1][0] & 4) != 4 && array[a][b - 1][1] == (v - 1)) {
                    v--;
                    array[a][b][3] = v;
                    b--;
                    continue;

                }
                if (b != (cols - 1) && (array[a][b + 1][0] & 1) != 1 && array[a][b + 1][1] == (v - 1)) {
                    v--;
                    array[a][b][3] = v;
                    b++;
                }
            }
        }

    }
}


