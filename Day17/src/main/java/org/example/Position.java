package org.example;

import java.util.Objects;

import static org.example.Direction.*;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Position getPositionAfterMove(Direction direction) {
        if(direction == LEFT) {
            return new Position(this.getX(), this.getY() - 1);
        }
        if(direction == RIGHT) {
            return new Position(this.getX(), this.getY() + 1);
        }
        if(direction == UP) {
            return new Position(this.getX() - 1, this.getY());
        }
        if(direction == DOWN) {
            return new Position(this.getX() + 1, this.getY());
        }
        return null;
    }
}
