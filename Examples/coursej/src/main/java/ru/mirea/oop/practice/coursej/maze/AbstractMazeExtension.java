package ru.mirea.oop.practice.coursej.maze;

import ru.mirea.oop.practice.coursej.ext.MazeExtension;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractMazeExtension implements MazeExtension {
    @Override
    public BufferedImage createImage(Maze maze) {
        BufferedImage result = new BufferedImage(maze.cols, maze.rows, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawMaze(g, maze);
        return result;
    }

    private static void drawMaze(Graphics2D g, Maze maze) {

    }
}
