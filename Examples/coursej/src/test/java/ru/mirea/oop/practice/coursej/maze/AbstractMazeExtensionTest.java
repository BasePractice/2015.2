package ru.mirea.oop.practice.coursej.maze;

import org.junit.Before;
import org.junit.Test;
import ru.mirea.oop.practice.coursej.ext.MazeExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class AbstractMazeExtensionTest {

    private MazeExtension extension;

    @Before
    public void setUp() throws Exception {
        extension = new AbstractMazeExtension() {
            @Override
            public String description() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }

            @Override
            public Maze generateMaze(int rows, int cols) {
                Maze maze = new Maze(rows, cols);
                for (int row = 0; row < maze.rows; ++row) {
                    for (int col = 0; col < maze.cols; ++col) {
                        maze.data[row][col] |= MazeExtension.SQUARE_LEFT;
                        maze.data[row][col] |= MazeExtension.SQUARE_UP;
                        maze.data[row][col] |= MazeExtension.SQUARE_RIGHT;
                        maze.data[row][col] |= MazeExtension.SQUARE_DOWN;
                    }
                }
                return maze;
            }
        };
    }

    @Test
    public void testCreateImage() throws Exception {
        MazeExtension.Maze maze = extension.generateMaze(10, 10);
        BufferedImage image = extension.createImage(maze);
        File file = new File(extension.name() + ".png");
        ImageIO.write(image, "PNG", file);
    }
}