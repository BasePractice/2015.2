package ru.mirea.oop.practice.coursej.s131249;

import ru.mirea.oop.practice.coursej.api.ext.MazeExtension;


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
            this.left=true;
        }
        if ((c & MazeExtension.SQUARE_UP) == MazeExtension.SQUARE_UP) {
            this.up=true;
        }
        if ((c & MazeExtension.SQUARE_RIGHT) == MazeExtension.SQUARE_RIGHT) {
            this.right=true;
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
    public void newGroupId(long lastGroupId) {
        if (this.groupId == 0) {
            this.groupId = ++lastGroupId;
        }
    }


    public void clearGroupId() {
        this.groupId = 0;
    }
}
