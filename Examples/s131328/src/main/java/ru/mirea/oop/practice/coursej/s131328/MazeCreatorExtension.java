package ru.mirea.oop.practice.coursej.s131328;

import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

import java.util.LinkedList;
import java.util.Random;

public final class MazeCreatorExtension extends AbstractMazeExtension {
    @Override
    public String description() {
        return "Работа с лабиринтом при помощи алгоритма Рекурсивного обхода";
    }

    @Override
    public String name() {
        return "maze.services.Recursive";
    }

    //Массив клеток
    Cell[][] cells;
    //Всё те же клетки, находятся в множестве для быстрого доступа
    LinkedList<Cell> nonCheckCells = new LinkedList<>();
    //Путь алгоритма
    LinkedList<Cell> stack = new LinkedList<>();
    //Список для хранения соседей отдельно взятой клетки
    LinkedList<Cell> neighbors;
    //Общий объект для слуайной выборки

    Random random;

    @Override
    public Maze generateMaze(int rows, int cols) {
        nonCheckCells = new LinkedList<>();
        cells = new Cell[rows][cols];
        stack = new LinkedList<>();


        random = new Random();

        //Заполняем массив новыми клетками(Cell)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
                nonCheckCells.add(cells[i][j]);
            }
        }


        while (nonCheckCells.size() != 0) {
            if (stack.size() == 0) {
                addToStack(nonCheckCells.get(random.nextInt(nonCheckCells.size())));//Добавляем первую клетку
            } else {
                while (getNeighbors(stack.getLast(), rows, cols).size() == 0)
                    stack.removeLast();
            }
            while ((neighbors = getNeighbors(stack.getLast(), rows, cols)).size() != 0) {
                addToStack(neighbors.get(random.nextInt(neighbors.size())));
            }
        }

        //Преобразуем к интересующему формату
        Maze maze = new Maze(rows, cols);
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                maze.data[x][y] = cells[x][y].getValue();
            }
        }

        return maze;
    }

    //Добавляет новый элемент к нашему пути
    void addToStack(Cell cell) {
        if (stack.size() != 0)
            setCorridor(cell, stack.getLast());

        stack.add(cell);
        cell.check();
        nonCheckCells.remove(cell);
    }

    //Устанавливает коридор между двумя клетками
    void setCorridor(Cell firstCell, Cell secondCell) {
        switch (firstCell.x - secondCell.x) {
            case 0:
                break;
            case 1:
                firstCell.LEFT = false;
                secondCell.RIGHT = false;
                return;
            case -1:
                firstCell.RIGHT = false;
                secondCell.LEFT = false;
                return;
        }
        switch (firstCell.y - secondCell.y) {
            case 1:
                firstCell.UP = false;
                secondCell.DOWN = false;
                return;
            case -1:
                firstCell.DOWN = false;
                secondCell.UP = false;
                break;
        }

    }

    //Создает и возвращает список существующих соседей
    LinkedList<Cell> getNeighbors(Cell cell, int rows, int cols) {
        LinkedList<Cell> Neighbors = new LinkedList<>();
        if (cell.x != 0)
            if (!cells[cell.x - 1][cell.y].isCheck)//Клетка слева
                Neighbors.add(cells[cell.x - 1][cell.y]);
        if (cell.y != 0)
            if (!cells[cell.x][cell.y - 1].isCheck)//Клетка сверху
                Neighbors.add(cells[cell.x][cell.y - 1]);
        if (cell.x != rows - 1)
            if (!cells[cell.x + 1][cell.y].isCheck)//Клетка справа
                Neighbors.add(cells[cell.x + 1][cell.y]);
        if (cell.y != cols - 1)
            if (!cells[cell.x][cell.y + 1].isCheck)//Клетка снизу
                Neighbors.add(cells[cell.x][cell.y + 1]);

        return Neighbors;
    }

    class Cell {

        int x;
        int y;
        boolean isCheck;
        boolean UP;
        boolean LEFT;
        boolean DOWN;
        boolean RIGHT;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
            isCheck = false;
            UP = true;
            LEFT = true;
            DOWN = true;
            RIGHT = true;
        }


        public char getValue() {
            char val = 0;
            if (UP) val |= SQUARE_UP;
            if (LEFT) val |= SQUARE_LEFT;
            if (DOWN) val |= SQUARE_DOWN;
            if (RIGHT) val |= SQUARE_RIGHT;
            return val;
        }

        public void check() {
            isCheck = true;
        }
    }
}


