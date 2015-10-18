package ru.mirea.oop.practice.coursej.ext;

import java.awt.image.BufferedImage;

public interface MazeExtension {
    String description();

    String name();

    Maze generateMaze(int width, int height);

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
}
