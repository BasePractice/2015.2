package ru.mirea.oop.practice.coursej.maze;

import ru.mirea.oop.practice.coursej.ext.MazeExtension;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractMazeExtension implements MazeExtension {
    private static final int WIDTH = 20;

    @Override
    public BufferedImage createImage(Maze maze) {
        BufferedImage result = new BufferedImage(maze.cols * WIDTH * 2, maze.rows * WIDTH * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawMaze(g, maze, Color.DARK_GRAY, WIDTH, 1);
        return result;
    }

    static void drawMaze(Graphics2D g, Maze maze, Color color, int width, int thickness) {
        g.setColor(color);
        for (int row = 0; row < maze.rows; ++row) {
            for (int col = 0; col < maze.cols; ++col) {
                int halfWidth = width / 2;
                int xCenter = (row * width) + halfWidth;
                int yCenter = (col * width) + halfWidth;

                g.setColor(Color.RED);
                drawCenteredCircle(g, xCenter, yCenter, thickness);
                g.setColor(color);
                if ((maze.data[row][col] & MazeExtension.SQUARE_LEFT) == MazeExtension.SQUARE_LEFT) {
                    g.drawLine(xCenter - halfWidth, yCenter + halfWidth, xCenter - halfWidth, yCenter - halfWidth);
                }
                if ((maze.data[row][col] & MazeExtension.SQUARE_UP) == MazeExtension.SQUARE_UP) {
                    g.drawLine(xCenter - halfWidth, yCenter - halfWidth, xCenter + halfWidth, yCenter - halfWidth);
                }
                if ((maze.data[row][col] & MazeExtension.SQUARE_RIGHT) == MazeExtension.SQUARE_RIGHT) {
                    g.drawLine(xCenter + halfWidth, yCenter - halfWidth, xCenter + halfWidth, yCenter + halfWidth);
                }
                if ((maze.data[row][col] & MazeExtension.SQUARE_DOWN) == MazeExtension.SQUARE_DOWN) {
                    g.drawLine(xCenter - halfWidth, yCenter + halfWidth, xCenter + halfWidth, yCenter + halfWidth);
                }
            }
        }
    }

    private static void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x = x - (r / 2);
        y = y - (r / 2);
        g.fillOval(x, y, r, r);
    }
}
