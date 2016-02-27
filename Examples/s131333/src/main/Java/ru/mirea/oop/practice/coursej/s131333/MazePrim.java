package ru.mirea.oop.practice.coursej.s131333;


import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

import java.util.LinkedList;
import java.util.Random;

public final class MazePrim extends AbstractMazeExtension {
    LinkedList<Cell> outsideCells = new LinkedList<>();
    LinkedList<Cell> insideCells = new LinkedList<>();
    LinkedList<Cell> adjacentCells = new LinkedList<>();
    LinkedList<Cell> closestCells = new LinkedList<>();
    LinkedList<Cell> waveCells = new LinkedList<>();
    LinkedList<Cell> nextWave = new LinkedList<>();
    Cell[][] cells;
    Random random;
    private int rows;
    private int cols;
    private Cell closestCell;
    private Cell currentCell;
    private int wave;

    @Override
    public String description() {
        return "Работа с лабиринтом при помощи алгоритма Прима";
    }

    @Override
    public String name() {
        return "maze.services.MazePrim";
    }

    @Override
    public Maze generateMaze(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
        random = new Random();
        cells = new Cell[rows][cols];

        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                cells[i][j] = new Cell(i, j);
                outsideCells.add(cells[i][j]);
            }
        }

        // Добавление первой клетки в лабиринт, а ёё соседей - в список смежных клеток
        Cell first = outsideCells.get(random.nextInt(outsideCells.size()));
        insideCells.add(first);
        first.check();

        if (first.x != 0)
            if (!cells[first.x - 1][first.y].isChecked) {                    //westWall
                adjacentCells.add(cells[first.x - 1][first.y]);
                outsideCells.remove(cells[first.x - 1][first.y]);
            }
        if (first.y != 0)
            if (!cells[first.x][first.y - 1].isChecked) {                    //northWall
                adjacentCells.add(cells[first.x][first.y - 1]);
                outsideCells.remove(cells[first.x][first.y - 1]);
            }
        if (first.x != rows - 1)
            if (!cells[first.x + 1][first.y].isChecked) {                    //eastWall
                adjacentCells.add(cells[first.x + 1][first.y]);
                outsideCells.remove(cells[first.x + 1][first.y]);
            }
        if (first.y != cols - 1)
            if (!cells[first.x][first.y + 1].isChecked) {                    //southWall
                adjacentCells.add(cells[first.x][first.y + 1]);
                outsideCells.remove(cells[first.x][first.y + 1]);
            }

        outsideCells.remove(first);

        //Добавляем клетки к лабиринту до тех пор пока список смежных клеток не опустеет
        while (!adjacentCells.isEmpty()) {
            addCell(adjacentCells.get(random.nextInt(adjacentCells.size())));
        }

        Maze maze = new Maze(rows, cols);
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                maze.data[x][y] = cells[x][y].getValue();
            }
        }

        return maze;
    }

    void makeWave(LinkedList<Cell> waveCells) { //Работа с текущей волной
        while (!waveCells.isEmpty()) {
            currentCell = waveCells.getFirst();
            workWithCurrentWave(currentCell);
            waveCells.remove(currentCell);
        }
    }

    //Добавление клеток следующей волны в список nextWave и указание расстояния
    void workWithCurrentWave(Cell currentCell) {
        if (currentCell.x != 0) {
            if (!cells[currentCell.x][currentCell.y].isWestWall()
                    & cells[currentCell.x - 1][currentCell.y].distance == -1) {
                cells[currentCell.x - 1][currentCell.y].distance = wave + 1;
                nextWave.add(cells[currentCell.x - 1][currentCell.y]);
            }
        }
        if (currentCell.x != rows - 1) {
            if (!cells[currentCell.x][currentCell.y].isEastWall()
                    & cells[currentCell.x + 1][currentCell.y].distance == -1) {
                cells[currentCell.x + 1][currentCell.y].distance = wave + 1;
                nextWave.add(cells[currentCell.x + 1][currentCell.y]);
            }
        }
        if (currentCell.y != 0) {
            if (!cells[currentCell.x][currentCell.y].isNorthWall()
                    & cells[currentCell.x][currentCell.y - 1].distance == -1) {
                cells[currentCell.x][currentCell.y - 1].distance = wave + 1;
                nextWave.add(cells[currentCell.x][currentCell.y - 1]);
            }
        }
        if (currentCell.y != cols - 1) {
            if (!cells[currentCell.x][currentCell.y].isSouthWall()
                    & cells[currentCell.x][currentCell.y + 1].distance == -1) {
                cells[currentCell.x][currentCell.y + 1].distance = wave + 1;
                nextWave.add(cells[currentCell.x][currentCell.y + 1]);
            }
        }
    }

    @Override
    public Point[] findPath(Maze maze) {
        cols = maze.cols;
        rows = maze.rows;
        Cell start = cells[0][0];
        Cell finish = cells[rows - 1][cols - 1];
        start.distance = 0;
        wave = 0;
        //Добавление клеток первой волны
        if (start.x != 0) {
            if (!cells[start.x][start.y].isWestWall() & cells[start.x - 1][start.y].distance == -1) {
                cells[start.x - 1][start.y].distance = wave + 1;
                waveCells.add(cells[start.x - 1][start.y]);
            }
        }
        if (start.y != rows - 1) {
            if (!cells[start.x][start.y].isEastWall() & cells[start.x + 1][start.y].distance == -1) {
                cells[start.x + 1][start.y].distance = wave + 1;
                waveCells.add(cells[start.x + 1][start.y]);
            }
        }
        if (start.y != 0) {
            if (!cells[start.x][start.y].isNorthWall() & cells[start.x][start.y - 1].distance == -1) {
                cells[start.x][start.y - 1].distance = wave + 1;
                waveCells.add(cells[start.x][start.y - 1]);
            }
        }
        if (start.y != cols - 1) {
            if (!cells[start.x][start.y].isSouthWall() & cells[start.x][start.y + 1].distance == -1) {
                cells[start.x][start.y + 1].distance = wave + 1;
                waveCells.add(cells[start.x][start.y + 1]);
            }
        }

        wave++;

        while (finish.distance == -1) { //Условие остановки
            makeWave(waveCells);
            waveCells = (LinkedList) nextWave.clone();
            nextWave.clear();
            wave++;
        }

        LinkedList<Cell> pathPoints = new LinkedList<>();
        pathPoints.add(finish);
        currentCell = finish;
        //Обратный ход алгоритма
        do {
            if (!currentCell.isWestWall()
                    && (currentCell.distance - cells[currentCell.x - 1][currentCell.y].distance == 1)) {
                currentCell = cells[currentCell.x - 1][currentCell.y];
                pathPoints.add(currentCell);
            }
            if (!currentCell.isEastWall()
                    && (currentCell.distance - cells[currentCell.x + 1][currentCell.y].distance == 1)) {
                currentCell = cells[currentCell.x + 1][currentCell.y];
                pathPoints.add(currentCell);
            }
            if (!currentCell.isNorthWall()
                    && (currentCell.distance - cells[currentCell.x][currentCell.y - 1].distance == 1)) {
                currentCell = cells[currentCell.x][currentCell.y - 1];
                pathPoints.add(currentCell);
            }
            if (!currentCell.isSouthWall()
                    && (currentCell.distance - cells[currentCell.x][currentCell.y + 1].distance == 1)) {
                currentCell = cells[currentCell.x][currentCell.y + 1];
                pathPoints.add(currentCell);
            }
        } while (!pathPoints.contains(start));

        Point[] path = new Point[pathPoints.size()];
        for (int i = 0; i < path.length; i++) {
            path[i] = new Point(pathPoints.get(i).x, pathPoints.get(i).y);
        }
        return path;
    }

    void addCell(Cell cell) {   //Добавление клетки в лабиринт
        cell.check();
        if (cell.x != 0)
            if (!cells[cell.x - 1][cell.y].isChecked
                    & !insideCells.contains(cells[cell.x - 1][cell.y])
                    & !adjacentCells.contains(cells[cell.x - 1][cell.y]))//westWall
            {
                adjacentCells.add(cells[cell.x - 1][cell.y]);
                outsideCells.remove(cells[cell.x - 1][cell.y]);
            }
        if (cell.y != 0)
            if (!cells[cell.x][cell.y - 1].isChecked
                    & !insideCells.contains(cells[cell.x][cell.y - 1])
                    & !adjacentCells.contains(cells[cell.x][cell.y - 1]))//northWall   // добавляем соседей новой клетки
            {
                adjacentCells.add(cells[cell.x][cell.y - 1]);
                outsideCells.remove(cells[cell.x][cell.y - 1]);
            }
        if (cell.x != rows - 1)
            if (!cells[cell.x + 1][cell.y].isChecked
                    & !insideCells.contains(cells[cell.x + 1][cell.y])
                    & !adjacentCells.contains(cells[cell.x + 1][cell.y]))//eastWall
            {
                adjacentCells.add(cells[cell.x + 1][cell.y]);
                outsideCells.remove(cells[cell.x + 1][cell.y]);
            }
        if (cell.y != cols - 1)
            if (!cells[cell.x][cell.y + 1].isChecked
                    & !insideCells.contains(cells[cell.x][cell.y + 1])
                    & !adjacentCells.contains(cells[cell.x][cell.y + 1]))   //southWall
            {
                adjacentCells.add(cells[cell.x][cell.y + 1]);
                outsideCells.remove(cells[cell.x][cell.y + 1]);
            }
        getClosestInside(cell);
        connectCells(closestCell, cell);
        adjacentCells.remove(cell);
        insideCells.add(cell);
    }

    //Выбор соседней уже добавленной в лабиринт клетки для новой добавленной клетки
    Cell getClosestInside(Cell cell) {
        closestCells.clear();
        if (cell.x != 0 && insideCells.contains(cells[cell.x - 1][cell.y]))
            closestCells.add(cells[cell.x - 1][cell.y]);
        if (cell.y != 0 && insideCells.contains(cells[cell.x][cell.y - 1]))
            closestCells.add(cells[cell.x][cell.y - 1]);
        if (cell.x != rows - 1 && insideCells.contains(cells[cell.x + 1][cell.y]))
            closestCells.add(cells[cell.x + 1][cell.y]);
        if (cell.y != cols - 1 && insideCells.contains(cells[cell.x][cell.y + 1]))
            closestCells.add(cells[cell.x][cell.y + 1]);

        closestCell = closestCells.get(random.nextInt(closestCells.size()));
        return closestCell;
    }

    void connectCells(Cell firstCell, Cell secondCell) {    //Соединяем 2 клетки
        switch (firstCell.x - secondCell.x) {
            case 0:
                break;
            case 1:
                firstCell.setWestWall(false);
                secondCell.setEastWall(false);
                return;
            case -1:
                firstCell.setEastWall(false);
                secondCell.setWestWall(false);
                return;
        }
        switch (firstCell.y - secondCell.y) {
            case 1:
                firstCell.setNorthWall(false);
                secondCell.setSouthWall(false);
                return;
            case -1:
                firstCell.setSouthWall(false);
                secondCell.setNorthWall(false);
                break;
        }
    }

    private final class Cell {
        final int x;
        final int y;
        boolean isChecked = false;
        int distance = -1;    //Расстояние до старта лабиринта
        private boolean northWall = true;
        private boolean westWall = true;
        private boolean southWall = true;
        private boolean eastWall = true;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public char getValue() {
            char val = 0;
            if (isNorthWall()) val |= SQUARE_UP;
            if (isWestWall()) val |= SQUARE_LEFT;
            if (isSouthWall()) val |= SQUARE_DOWN;
            if (isEastWall()) val |= SQUARE_RIGHT;
            return val;
        }

        public void check() {
            isChecked = true;
        }

        public boolean isNorthWall() {
            return northWall;
        }

        public void setNorthWall(boolean northWall) {
            this.northWall = northWall;
        }

        public boolean isWestWall() {
            return westWall;
        }

        public void setWestWall(boolean westWall) {
            this.westWall = westWall;
        }

        public boolean isSouthWall() {
            return southWall;
        }

        public void setSouthWall(boolean southWall) {
            this.southWall = southWall;
        }

        public boolean isEastWall() {
            return eastWall;
        }

        public void setEastWall(boolean eastWall) {
            this.eastWall = eastWall;
        }
    }
}
