package ru.mirea.oop.practice.coursej.s131328;

import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

import java.util.LinkedList;

public final class MazeCreatorExtension extends AbstractMazeExtension {
    @Override
    public String description() {
        return "Работа с лабиринтом при помощи алгоритма Рекурсивного обхода";
    }

    @Override
    public String name() {
        return "maze.services.Recursive";
    }

    @Override
    public Maze generateMaze(int rows, int cols) {
//        Cell[][] cells = new Cell[rows][cols];
//        for (int i = 0; i < rows; i++){
//            for (int j = 0; j < cols; j++) {
//                cells[i][j] = new Cell(i, j, SQUARE_LEFT+SQUARE_RIGHT+SQUARE_UP+SQUARE_DOWN);
//            }
//        }

        int[][] cells = new int[rows][cols];

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++) {
                cells[i][j] = SQUARE_LEFT+SQUARE_RIGHT+SQUARE_UP+SQUARE_DOWN;
            }
        }


        //LinkedList<Cell> stack = new LinkedList<Cell>();
        Maze maze = new Maze(rows, cols);

        for(int x = 0; x < rows; x++)
        {
            for(int y = 0; y < cols; y++)
            {
                maze.data[x][y] = (char)cells[x][y];
            }
        }

        return maze;
    }

    class Cell{

        public int x, y, value;

        public Cell(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }
}

