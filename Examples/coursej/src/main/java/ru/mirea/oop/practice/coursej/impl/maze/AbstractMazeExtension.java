package ru.mirea.oop.practice.coursej.impl.maze;

import ru.mirea.oop.practice.coursej.api.ext.MazeExtension;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractMazeExtension implements MazeExtension {
    private static final int WIDTH = 20;
    private static final int THICKNESS = 3;

    public Point[] findPath(Maze maze) {
        return null;
    }

    @Override
    public BufferedImage createImage(Maze maze, Point[] path) {
        BufferedImage result = new BufferedImage(maze.cols * WIDTH + WIDTH, maze.rows * WIDTH + WIDTH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawMaze(g, maze, Color.DARK_GRAY, WIDTH, THICKNESS);
        if (path != null)
            drawPath(g, path, Color.ORANGE, WIDTH, THICKNESS);
        return result;
    }

    @Override
    public BufferedImage createImage(Maze maze) {
        return createImage(maze, null);
    }

    static void drawPath(Graphics2D g, Point[] path, Color color, int width, int thickness) {
        final Stroke lastStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness));
        Color lastColor = g.getColor();
        g.setColor(color);
        int lastX = -1;
        int lastY = -1;
        for (Point p : path) {
            int halfWidth = width / 2;
            int xCenter = (p.x * width) + halfWidth;
            int yCenter = (p.y * width) + halfWidth;
            if (lastX >= 0 && lastY >= 0) {
                g.drawLine(lastX, lastY, xCenter, yCenter);
            }
            lastX = xCenter;
            lastY = yCenter;
        }
        g.setStroke(lastStroke);
        g.setColor(lastColor);
    }

    static void drawMaze(Graphics2D g, Maze maze, Color color, int width, int thickness) {
        final Stroke lastStroke = g.getStroke();
        g.setStroke(new BasicStroke(thickness));
        Color lastColor = g.getColor();
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
        g.setStroke(lastStroke);
        g.setColor(lastColor);
    }

    private static void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x = x - (r / 2);
        y = y - (r / 2);
        g.fillOval(x, y, r, r);
    }
}
