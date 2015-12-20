package ru.mirea.oop.practice.coursej.s131243;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

import java.util.*;

public class BinaryTreeMaze extends AbstractMazeExtension {
    private final Random random = new Random();
    private int rows;
    private int cols;

    private static final Logger logger = LoggerFactory.getLogger(BinaryTreeMaze.class);

    public String description() {
        return "Лабиринт по алгоритму Бинарного дерева";
    }

    public String name() {
        return "maze.services.BinaryTreeMaze";
    }

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    @Override
    public Maze generateMaze(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;

        Cell[][] cells = new Cell[rows][cols];

        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                Cell nc = new Cell(i, j);
                if (j == 0) {
                    nc.right = false;
                    nc.left = false;
                    nc.up = true;
                } else {
                    nc.up = true;
                    nc.down = true;
                    nc.right = true;
                    if (getRandomBoolean() || i == 0) {
                        nc.up = false;
                        nc.left = true;
                        cells[i][j - 1].down = false;
                    } else {
                        nc.left = false;
                        cells[i - 1][j].right = false;
                    }
                }

                if (j == rows - 1) {
                    nc.down = true;
                }

                if (i == cols - 1) {
                    nc.right = true;
                }

                cells[i][j] = nc;
            }
        }

        Maze maze = new Maze(rows, cols);
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                maze.data[x][y] = cells[x][y].getValue();
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
                Cell cell = new Cell(x, y, maze.data[x][y]);
                cells[x][y] = cell;
                cellSet.add(cell);
            }
        }

        Cell start = cells[0][0];
        start.distance = 0;
        Cell finish = cells[rows - 1][cols - 1];
        int wave = 0;
        do {
            for (Cell cell : cellSet) {
                if (cell.distance == wave) {
                    List<Cell> neighbors = cell.getNeighborCells(cellSet);
                    for (Cell neighbor : neighbors) {
                        if (neighbor.distance == -1) {
                            neighbor.distance = wave + 1;
                        }
                    }
                }
            }
            wave++;
        } while (finish.distance == -1);
        List<Cell> path = new ArrayList<>();
        path.add(finish);
        Cell currCell = finish;
        while (!path.contains(start)) {
            assert currCell != null;
            currCell = getСlosest(currCell.getNeighborCells(cellSet));
            path.add(currCell);
        }
        logger.debug("Построен путь от входа к выходу длиной {}", wave);

        Point[] pathPoints = new Point[path.size()];
        for (int i = 0; i < pathPoints.length; i++) {
            pathPoints[i] = new Point(path.get(i).x, path.get(i).y);
        }

        return pathPoints;

    }

    public static Cell getСlosest(List<Cell> neighbors) {
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
