package org.example;

public class Position {

    private int x;
    private int y;

    private PositionType positionType;
    private boolean isEnergized;

    public Position(int x, int y, PositionType positionType) {
        this.x = x;
        this.y = y;
        this.positionType = positionType;
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

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public boolean isEnergized() {
        return isEnergized;
    }

    public void setEnergized(boolean energized) {
        isEnergized = energized;
    }
}
