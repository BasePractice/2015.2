package ru.mirea.oop.practice.coursej.api.ext;

import java.awt.image.BufferedImage;

public interface MazeExtension {
    String description();

    String name();

    Maze generateMaze(int rows, int cols);

    BufferedImage createImage(Maze maze);

    final class Maze {
        public final int cols;
        public final int rows;
        public final char[][] data;

        public Maze(int rows, int cols) {
            this.cols = cols;
            this.rows = rows;
            this.data = new char[rows][cols];
        }
    }

    char SQUARE_LEFT = 1;
    char SQUARE_UP = 2;
    char SQUARE_RIGHT = 4;
    char SQUARE_DOWN = 8;
}
