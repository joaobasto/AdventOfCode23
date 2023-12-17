package org.example;

import static org.example.Direction.*;

public class Beam {

    private boolean isMoving;

    private int x;
    private int y;
    private Direction direction;

    public Beam(boolean isMoving, int x, int y, Direction direction) {
        this.isMoving = isMoving;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move() {
        if(this.direction == LEFT) {
            this.y = this.y - 1;
        } else if(this.direction == RIGHT) {
            this.y = this.y + 1;
        } else if(this.direction == UP) {
            this.x = this.x - 1;
        }else if(this.direction == DOWN) {
            this.x = this.x + 1;
        }
    }

    public void stopIfBordersCrossed(int numberOfLines, int numberOfColumns) {
        if(this.x < 0 || this.x >= numberOfLines || this.y < 0 || this.y >= numberOfColumns) {
            this.isMoving = false;
        }
    }
}
