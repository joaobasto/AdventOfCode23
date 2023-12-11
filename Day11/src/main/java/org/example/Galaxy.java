package org.example;

public class Galaxy {

    private int id;

    //Coordinates are saved following the vector coordinates:
    // x -> direction north to south
    // y -> direction west to east
    private long x;
    private long y;

    public Galaxy(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }
}
