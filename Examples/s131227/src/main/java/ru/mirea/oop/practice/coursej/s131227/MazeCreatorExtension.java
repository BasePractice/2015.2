package ru.mirea.oop.practice.coursej.s131227;


import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

/**
 * -3
 * Не работает поиск пути
 */
public final class MazeCreatorExtension extends AbstractMazeExtension {

    int rows;
    int cols;

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

        MazeBlueprint mazeb = new MazeBlueprint(rows, cols);
        mazeb.fillMaze();
        mazeb.firstKill();
        mazeb.hunt();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                maze.data[i][j] = mazeb.getWalls(j, i);
            }
        }

        return maze;
    }

    @Override
    public Point[] findPath(Maze maze) {
        cols = maze.rows;
        rows = maze.cols;
        int[][][] map = new int[rows][cols][3];
        int w;
        int bw;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                map[i][j][0] = maze.data[j][i];
            }
        }
        w = 2;
        map[rows - 1][cols - 1][1] = 1;
        map[rows - 1][cols - 1][2] = 1;
        while(map[0][0][2] != 1) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (i != (rows - 1) && (map[i + 1][j][0] & 2) != 2 && map[i + 1][j][1] == (w - 1) &&
                            map[i][j][2] != 1) {
                        map[i][j][1] += w;
                        map[i][j][2] = 1;
                    }
                    if (j != 0 && (map[i][j - 1][0] & 4) != 4 && map[i][j - 1][1] == (w - 1) &&
                            map[i][j][2] != 1) {
                        map[i][j][1] += w;
                        map[i][j][2] = 1;
                    }
                    if (j != (cols - 1) && (map[i][j + 1][0] & 1) != 1 && map[i][j + 1][1] == (w - 1) &&
                            map[i][j][2] != 1) {
                        map[i][j][1] += w;
                        map[i][j][2] = 1;
                    }
                    if (i != 0 && (map[i - 1][j][0] & 8) != 8 && map[i - 1][j][1] == (w - 1) &&
                            map[i][j][2] != 1) {
                        map[i][j][1] += w;
                        map[i][j][2] = 1;
                    }
                }
            }
            w++;
        }
        Point[] pathPoints = new Point[w];
        pathPoints[0] = new Point(0, 0);
        int c = 1;
        int a = 0;
        int b = 0;
        bw = map[0][0][1];
        while(bw != 1){
            if (a != 0 && (map[a - 1][b][0] & 8) != 8 && map[a - 1][b][1] == (bw - 1)) {
                bw--;
                pathPoints[c] = new Point(b, a);
                c++;
                a--;
                continue;
            }
            if (a != (rows - 1) && (map[a + 1][b][0] & 2) != 2 && map[a + 1][b][1] == (bw - 1)) {
                bw--;
                pathPoints[c] = new Point(b, a);
                c++;
                a++;
                continue;

            }
            if (b != 0 && (map[a][b - 1][0] & 4) != 4 && map[a][b - 1][1] == (bw - 1)) {
                bw--;
                pathPoints[c] = new Point(b, a);
                c++;
                b--;
                continue;

            }
            if (b != (cols - 1) && (map[a][b + 1][0] & 1) != 1 && map[a][b + 1][1] == (bw - 1)) {
                bw--;
                pathPoints[c] = new Point(b, a);
                c++;
                b++;
            }
        }
        pathPoints[c] = new Point(rows - 1, cols - 1);

        return pathPoints;
    }
}