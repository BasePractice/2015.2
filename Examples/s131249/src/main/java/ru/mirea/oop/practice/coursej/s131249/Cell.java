package ru.mirea.oop.practice.coursej.s131249;

import ru.mirea.oop.practice.coursej.api.ext.MazeExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Cell {
    int x;
    int y;
    long groupId;
    boolean up;
    boolean left;
    boolean down;
    boolean right;
    int distance;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.groupId = 0;
        this.up = false;
        this.left = false;
        this.down = false;
        this.right = false;
    }

    public Cell(int x, int y, char c) {
        this.x = x;
        this.y = y;
        if ((c & MazeExtension.SQUARE_LEFT) == MazeExtension.SQUARE_LEFT) {
            this.left = true;
        }
        if ((c & MazeExtension.SQUARE_UP) == MazeExtension.SQUARE_UP) {
            this.up = true;
        }
        if ((c & MazeExtension.SQUARE_RIGHT) == MazeExtension.SQUARE_RIGHT) {
            this.right = true;
        }
        if ((c & MazeExtension.SQUARE_DOWN) == MazeExtension.SQUARE_DOWN) {
            this.down = true;
        }
        this.distance = -1;
    }

    public Cell(int x, int y, Cell other) {
        this.x = x;
        this.y = y;
        this.groupId = other.getGroupId();
        this.down = other.isDown();
        this.right = other.isRight();
    }

    public long getGroupId() {
        return groupId;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isRight() {
        return right;
    }

    public char getValue() {
        char val = 0;
        if (up) val |= MazeExtension.SQUARE_UP;
        if (left) val |= MazeExtension.SQUARE_LEFT;
        if (down) val |= MazeExtension.SQUARE_DOWN;
        if (right) val |= MazeExtension.SQUARE_RIGHT;
        return val;
    }

    public boolean isNeighbor(Cell otherCell) {
        return Math.abs(this.x - otherCell.x) + Math.abs(this.y - otherCell.y) == 1;
    }

    public boolean canMoveTo(Cell to) {
        if (this.isNeighbor(to)) {
            switch (this.x - to.x) {
                case 0: {
                    switch (this.y - to.y) {
                        case 1: {
                            if (!this.up && !to.down) {
                                return true;
                            }
                            break;
                        }
                        case -1: {
                            if (!this.down && !to.up) {
                                return true;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    if (!this.left && !to.right) {
                        return true;
                    }
                    break;
                }
                case -1: {
                    if (!this.right && !to.left) {
                        return true;
                    }
                    break;
                }

            }
        }
        return false;
    }

    public List<Cell> getNeighbors(Set<Cell> cells) {
        List<Cell> neighbors = new ArrayList<>();
        for (Cell cell2 : cells) {
            if (this.isNeighbor(cell2) && this.canMoveTo(cell2)) {
                neighbors.add(cell2);
            }
        }
        return neighbors;
    }

}
