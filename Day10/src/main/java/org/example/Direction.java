package org.example;

import java.util.Map;

/**
 * Represents possible directions of movement.
 * For each direction, we save the vector of movement associated with it.
 * The coordinate vectors are X, which points from north to south,
 * and Y, which points from west to east.
 */
public enum Direction {

    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST( 0, -1);

    private static final Map<Direction, Direction> INVERSE_DIRECTIONS = Map.of(
            NORTH, SOUTH,
            SOUTH, NORTH,
            EAST, WEST,
            WEST, EAST
    );

    private int x;
    private int y;

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * returns the inverse direction of the input direction
     * @return
     */
    public static Direction getInverseDirection(Direction direction) {
        return INVERSE_DIRECTIONS.get(direction);
    }
}
