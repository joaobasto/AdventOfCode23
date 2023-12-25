package org.example;

public enum Direction {

    RIGHT,
    DOWN,
    LEFT,
    UP;

    public static Direction getDirectionFromString(String directionString) {
        if (directionString.equals("R")) {
            return RIGHT;
        }
        if (directionString.equals("L")) {
            return LEFT;
        }
        if (directionString.equals("U")) {
            return UP;
        }
        if (directionString.equals("D")) {
            return DOWN;
        }
        return null;
    }
}