package ru.mirea.oop.practice.coursej.s131249;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

import java.util.*;

/**
 * -4?
 */
public final class MazeEller extends AbstractMazeExtension {
    private static final Logger logger = LoggerFactory.getLogger(MazeEller.class);

    @Override
    public Point[] findPath(Maze maze) {
        cols = maze.cols;
        rows = maze.rows;
        Cell[][] cells = new Cell[maze.rows][maze.cols];
        Set<Cell> cellSet = new HashSet<>();
        for (int y = 0; y < maze.rows; y++) {
            for (int x = 0; x < maze.cols; x++) {
                //TODO: Ошибка? x и y не в разных местах стоят
                Cell cell = new Cell(y, x, maze.data[y][x]);
                cells[y][x] = cell;
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
                    List<Cell> neighbors = cell.getNeighbors(cellSet);
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
            currCell = getNearest(currCell.getNeighbors(cellSet));
            path.add(currCell);
        }
        logger.debug("найден путь от выхода до входа, его длинна {}", wave);

        Point[] pathPoints = new Point[path.size()];
        for (int i = 0; i < pathPoints.length; i++) {
            pathPoints[i] = new Point(path.get(i).x, path.get(i).y);
        }
        logger.debug("обратный путь построен");
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

    @Override
    public String description() {
        return "Лабиринт по алгаритму Эллера";
    }

    @Override
    public String name() {
        return "maze.services.MazeEller";
    }

    private final Random random = new Random();
    private long lastGroupId = 0L;
    private int rows;
    private int cols;

    @Override
    public Maze generateMaze(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
        List<Cell[]> cells = new ArrayList<>();
        cells.add(createFirstRow());
        for (int i = 0; i < rows - 2; i++) {
            cells.add(createRow(cells.get(i)));
        }
        cells.add(createLastRow(cells.get(rows - 2)));

        Maze maze = new Maze(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    cells.get(i)[j].up = true;
                }
                if (i == rows - 1) {
                    cells.get(i)[j].down = true;
                }
                if (j == 0) {
                    cells.get(i)[j].left = true;
                }
                if (j == cols - 1) {
                    cells.get(i)[j].right = true;
                }
                maze.data[j][i] = cells.get(i)[j].getValue();

            }

        }

        return maze;
    }

    void tryCreateWall(Cell cell1, Cell cell2, Cell[] row) {

        if (cell1.groupId != cell2.groupId) {
            if (random.nextBoolean()) {
                cell1.right = true;
            } else {
                long tmpId = cell2.groupId;
                joinGroup(cell1.groupId, tmpId, row);
            }
        } else {
            cell1.right = true;
        }
    }

    void tryCreateFloor(Cell cell, Cell[] row) {
        int cellsWithFloor = 0;
        int cellsInGroup = 0;
        for (Cell cellFromRow : row) {
            if (cellFromRow.groupId == cell.groupId) {
                cellsInGroup++;
                if (cellFromRow.down) {
                    cellsWithFloor++;
                }
            }
        }
        if (cellsInGroup > cellsWithFloor + 1) {
            cell.down = random.nextBoolean();
        }
    }


    Cell[] createFirstRow() {
        Cell[] row = new Cell[cols];
        for (int i = 0; i < cols; i++) {
            row[i] = new Cell(i, 0);
            row[i].groupId = ++lastGroupId;
        }
        for (int i = 0; i < cols - 1; i++) {
            tryCreateWall(row[i], row[i + 1], row);
        }
        for (int i = 0; i < cols; i++) {
            tryCreateFloor(row[i], row);
        }
        return row;
    }

    Cell[] createRow(Cell[] prevRow) {
        Cell[] row = new Cell[cols];
        for (int i = 0; i < cols; i++) {
            row[i] = new Cell(i, prevRow[0].y + 1, prevRow[i]);
            row[i].right = false;
        }
        for (int i = 0; i < cols; i++) {
            if (row[i].down) {
                row[i].groupId = 0;
                row[i].groupId = ++lastGroupId;
                row[i].down = false;

            }
        }
        for (int i = 0; i < cols - 1; i++) {
            tryCreateWall(row[i], row[i + 1], row);
        }
        for (int i = 0; i < cols; i++) {
            tryCreateFloor(row[i], row);
        }
        return row;
    }

    Cell[] createLastRow(Cell[] prevRow) {
        Cell[] row = createRow(prevRow);
        for (int i = 0; i < row.length - 1; i++) {
            if (row[i].groupId != row[i + 1].groupId) {
                row[i].right = false;
                long tmpId = row[i + 1].groupId;
                joinGroup(row[i].groupId, tmpId, row);
            }
        }
        return row;
    }

    static void joinGroup(long groupId1, long groupId2, Cell[] row) {
        for (Cell cell : row) {
            if (cell.groupId == groupId2) {
                cell.groupId = groupId1;
            }
        }
    }
}




