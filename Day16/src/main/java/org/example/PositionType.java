package org.example;

import java.util.List;

import static org.example.Direction.*;

public enum PositionType {

    GROUND,
    LEFT_UPPER_CORNER_MIRROR,
    RIGHT_UPPER_CORNER_MIRROR,
    HORIZONTAL_SPLITTER,
    VERTICAL_SPLITTER;

    public static PositionType getPositionTypeFromCharacter(Character c) {
        if(c.equals('.')) {
            return GROUND;
        } else if(c.equals('/')) {
            return RIGHT_UPPER_CORNER_MIRROR;
        } else if(c.equals('\\')) {
            return LEFT_UPPER_CORNER_MIRROR;
        } else if(c.equals('-')) {
            return HORIZONTAL_SPLITTER;
        } else if(c.equals('|')) {
            return VERTICAL_SPLITTER;
        }
        return GROUND;
    }

    public List<Direction> determineNewDirection(Direction direction) {
        if (this == GROUND) {
            return List.of(direction);
        } else if (this == RIGHT_UPPER_CORNER_MIRROR) {
            if (direction == LEFT) {
                return List.of(DOWN);
            } else if (direction == UP) {
                return List.of(RIGHT);
            } else if (direction == RIGHT) {
                return List.of(UP);
            } else if (direction == DOWN) {
                return List.of(LEFT);
            }
        } else if (this == LEFT_UPPER_CORNER_MIRROR) {
            if (direction == LEFT) {
                return List.of(UP);
            } else if (direction == UP) {
                return List.of(LEFT);
            } else if (direction == RIGHT) {
                return List.of(DOWN);
            } else if (direction == DOWN) {
                return List.of(RIGHT);
            }
        } else if (this == HORIZONTAL_SPLITTER) {
            if(direction == LEFT || direction == RIGHT) {
                return List.of(direction);
            } else {
                return List.of(LEFT, RIGHT);
            }
        } else if (this == VERTICAL_SPLITTER) {
            if(direction == UP || direction == DOWN) {
                return List.of(direction);
            } else {
                return List.of(UP, DOWN);
            }
        }
        return List.of();
    }
}
