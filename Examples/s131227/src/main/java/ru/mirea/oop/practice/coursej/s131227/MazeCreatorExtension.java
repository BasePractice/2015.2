package ru.mirea.oop.practice.coursej.s131227;


import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

import java.util.*;

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

        int x, y, a, b, c, d;
        int fullness = 0;

        for (x = 0; x < rows; x++) {
            for (y = 0; y < cols; y++) {
                maze.data[x][y] = 0;
            }
        }
        x = rnd.nextInt(rows);
        y = rnd.nextInt(cols);
        maze.data[x][y] = 15;
        while (((x != 0 && maze.data[x - 1][y] == 0) || (x != (rows - 1) && maze.data[x + 1][y] == 0) ||
                (y != 0 && maze.data[x][y - 1] == 0) || (y != (cols - 1) && maze.data[x][y + 1] == 0))) {
            c = (rnd.nextInt(3) - 1);
            d = (rnd.nextInt(3) - 1);
            if (c == -1 && d == 0 && (x != 0 && maze.data[x + c][y + d] == 0)) {
                maze.data[x][y] -= 2;
                x -= 1;
                maze.data[x][y] = 7;
            }
            if (c == 1 && d == 0 && (x != (rows - 1) && maze.data[x + c][y + d] == 0)) {
                maze.data[x][y] -= 8;
                x += 1;
                maze.data[x][y] = 13;
            }
            if (c == 0 && d == -1 && (y != 0 && maze.data[x + c][y + d] == 0)) {
                maze.data[x][y] -= 1;
                y -= 1;
                maze.data[x][y] = 11;
            }
            if (c == 0 && d == 1 && (y != (cols - 1) && maze.data[x + c][y + d] == 0)) {
                maze.data[x][y] -= 4;
                y += 1;
                maze.data[x][y] = 14;
            }
        }
        while(fullness != rows * cols){
            fullness = 0;
        for (x = 0; x < rows; x++) {
            for (y = 0; y < cols; y++) {
                if ((x != (rows - 1) && maze.data[x][y] == 0 && maze.data[x + 1][y] != 0) ||
                        (y != 0 && maze.data[x][y - 1] != 0 && maze.data[x][y] == 0) ||
                        (y != (cols - 1) && maze.data[x][y] == 0 && maze.data[x][y + 1] != 0) ||
                        (x != 0 && maze.data[x - 1][y] != 0 && maze.data[x][y] == 0)) {
                    if (x != (rows - 1) && maze.data[x][y] == 0 && maze.data[x + 1][y] != 0) {
                        maze.data[x][y] = 7;
                        maze.data[x + 1][y] -= 2;
                    }
                    if (y != 0 && maze.data[x][y - 1] != 0 && maze.data[x][y] == 0) {
                        maze.data[x][y] = 14;
                        maze.data[x][y - 1] -= 4;
                    }
                    if (y != (cols - 1) && maze.data[x][y] == 0 && maze.data[x][y + 1] != 0) {
                        maze.data[x][y] = 11;
                        maze.data[x][y + 1] -= 1;
                    }
                    if (x != 0 && maze.data[x - 1][y] != 0 && maze.data[x][y] == 0) {
                        maze.data[x][y] = 13;
                        maze.data[x - 1][y] -= 8;
                    }
                    a = x;
                    b = y;
                    while (((x != 0 && maze.data[x - 1][y] == 0) || (x != (rows - 1) && maze.data[x + 1][y] == 0) ||
                            (y != 0 && maze.data[x][y - 1] == 0) || (y != (cols - 1) && maze.data[x][y + 1] == 0))) {
                        c = (rnd.nextInt(3) - 1);
                        d = (rnd.nextInt(3) - 1);
                        if (c == -1 && d == 0 && (x != 0 && maze.data[x + c][y + d] == 0)) {
                            maze.data[x][y] -= 2;
                            x -= 1;
                            maze.data[x][y] = 7;
                        }
                        if (c == 1 && d == 0 && (x != (rows - 1) && maze.data[x + c][y + d] == 0)) {
                            maze.data[x][y] -= 8;
                            x += 1;
                            maze.data[x][y] = 13;
                        }
                        if (c == 0 && d == -1 && (y != 0 && maze.data[x + c][y + d] == 0)) {
                            maze.data[x][y] -= 1;
                            y -= 1;
                            maze.data[x][y] = 11;
                        }
                        if (c == 0 && d == 1 && (y != (cols - 1) && maze.data[x + c][y + d] == 0)) {
                            maze.data[x][y] -= 4;
                            y += 1;
                            maze.data[x][y] = 14;
                        }
                    }
                    x = a;
                    y = b;
                }
            }
        }
            for (x = 0; x < rows; x++) {
                for (y = 0; y < cols; y++) {
                    if(maze.data[x][y] != 0){
                        fullness++;
                    }
                }
            }
        }

        return maze;
    }

    @Override
    public Point[] findPath(Maze maze) {
        cols = maze.cols;
        rows = maze.rows;
        Cell[][] cells = new Cell[maze.rows][maze.cols];
        Set<Cell> cellSet = new HashSet<>();
        for (int y = 0; y < maze.rows; y++) {
            for (int x = 0; x < maze.cols; x++) {
                Cell cell = new Cell(y, x, maze.data[y][x]);
                cells[y][x] = cell;
                cellSet.add(cell);
            }
        }

        Cell start = cells[0][0];
        start.distance = 0;
        Cell finish = cells[rows - 1][cols - 1];
        int wave = 0;
        while (finish.distance == -1){
            for (Cell cell : cellSet) {
                if (cell.distance == wave) {
                    List<Cell> neighbors = cell.getNeighbors(cellSet);
                    for (Cell neighbor : neighbors) {
                        if (neighbor.distance == -1) {
                            neighbor.distance = wave + 1;
                        }
                    }
                }
            }
            wave++;
        }
        List<Cell> path = new ArrayList<>();
        path.add(finish);
        Cell currCell = finish;
        while (!path.contains(start)) {
            currCell = getNearest(currCell.getNeighbors(cellSet));
            path.add(currCell);
        }

        Point[] pathPoints = new Point[path.size()];
        for (int i = 0; i < pathPoints.length; i++) {
            pathPoints[i] = new Point(path.get(i).x, path.get(i).y);
        }
        return pathPoints;

    }

    public static Cell getNearest(List<Cell> neighbors) {
        Collections.sort(neighbors, (o1, o2) -> {
            Integer d1 = o1.distance;
            Integer d2 = o2.distance;
            return d1.compareTo(d2);
        });
        for (Cell cell : neighbors) {
            if (cell.distance != -1) {
                return cell;
            }
        }
        return null;
    }
}


