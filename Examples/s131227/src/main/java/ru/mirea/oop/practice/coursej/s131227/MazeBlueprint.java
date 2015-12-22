package ru.mirea.oop.practice.coursej.s131227;

import java.util.Random;

public class MazeBlueprint {
    Random rnd = new Random();
    int rows;
    int cols;
    char[][] mazeb;
    int x;
    int y;
    int c;
    int d;
    int fullness = 0;

    public MazeBlueprint(int rows, int cols) {
        this.rows = cols;
        this.cols = rows;
        mazeb = new char[rows][cols];
    }

    public char getWalls(int x, int y){
        return mazeb[x][y];
    }

    public void fillMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.mazeb[i][j] = 0;
            }
        }
    }

    public void kill(int x, int y){
        this.x = x;
        this.y = y;
        while (((x != 0 && mazeb[x - 1][y] == 0) || (x != (rows - 1) && mazeb[x + 1][y] == 0) ||
                (y != 0 && mazeb[x][y - 1] == 0) || (y != (cols - 1) && mazeb[x][y + 1] == 0))) {
            c = (rnd.nextInt(3) - 1);
            d = (rnd.nextInt(3) - 1);
            if (c == -1 && d == 0 && (x != 0 && mazeb[x + c][y + d] == 0)) {
                mazeb[x][y] -= 2;
                x -= 1;
                mazeb[x][y] = 7;
            }
            if (c == 1 && d == 0 && (x != (rows - 1) && mazeb[x + c][y + d] == 0)) {
                mazeb[x][y] -= 8;
                x += 1;
                mazeb[x][y] = 13;
            }
            if (c == 0 && d == -1 && (y != 0 && mazeb[x + c][y + d] == 0)) {
                mazeb[x][y] -= 1;
                y -= 1;
                mazeb[x][y] = 11;
            }
            if (c == 0 && d == 1 && (y != (cols - 1) && mazeb[x + c][y + d] == 0)) {
                mazeb[x][y] -= 4;
                y += 1;
                mazeb[x][y] = 14;
            }
        }
    }

    public void firstKill(){
        x = rnd.nextInt(rows);
        y = rnd.nextInt(cols);
        mazeb[x][y] = 15;
        this.kill(x, y);
    }

    public void hunt(){
        while(fullness != rows * cols){
            fullness = 0;
            for(int i = 0; i < rows; i++){
                for(int j = 0; j < cols; j++){
                    if ((i != (rows - 1) && mazeb[i][j] == 0 && mazeb[i + 1][j] != 0) ||
                            (j != 0 && mazeb[i][j - 1] != 0 && mazeb[i][j] == 0) ||
                            (j != (cols - 1) && mazeb[i][j] == 0 && mazeb[i][j + 1] != 0) ||
                            (i != 0 && mazeb[i - 1][j] != 0 && mazeb[i][j] == 0)) {
                        if (i != (rows - 1) && mazeb[i][j] == 0 && mazeb[i + 1][j] != 0) {
                            mazeb[i][j] = 7;
                            mazeb[i + 1][j] -= 2;
                        }
                        if (j != 0 && mazeb[i][j - 1] != 0 && mazeb[i][j] == 0) {
                            mazeb[i][j] = 14;
                            mazeb[i][j - 1] -= 4;
                        }
                        if (j != (cols - 1) && mazeb[i][j] == 0 && mazeb[i][j + 1] != 0) {
                            mazeb[i][j] = 11;
                            mazeb[i][j + 1] -= 1;
                        }
                        if (i != 0 && mazeb[i - 1][j] != 0 && mazeb[i][j] == 0) {
                            mazeb[i][j] = 13;
                            mazeb[i - 1][j] -= 8;
                        }
                        this.kill(i, j);
                    }
                }
            }
            for(int i = 0; i < rows; i++){
                for(int j = 0; j < cols; j++){
                    if(mazeb[i][j] != 0){
                        fullness++;
                    }
                }
            }
        }
    }
}
