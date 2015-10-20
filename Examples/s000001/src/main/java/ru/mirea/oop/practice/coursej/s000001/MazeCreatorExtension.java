package ru.mirea.oop.practice.coursej.s000001;

import ru.mirea.oop.practice.coursej.impl.maze.AbstractMazeExtension;

public final class MazeCreatorExtension extends AbstractMazeExtension {
    @Override
    public String description() {
        return "Работа с лабиринтом при помощи алгоритма ...";
    }

    @Override
    public String name() {
        return "maze.services.Creator";
    }

    @Override
    public Maze generateMaze(int rows, int cols) {
        return new Maze(rows, cols);
    }
}
