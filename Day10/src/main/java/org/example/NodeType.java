package org.example;

import java.util.List;
import java.util.Map;

public enum NodeType {

    NORTH_SOUTH(List.of(Direction.NORTH, Direction.SOUTH)),
    EAST_WEST(List.of(Direction.EAST, Direction.WEST)),
    NORTH_EAST(List.of(Direction.NORTH, Direction.EAST)), //entering border
    NORTH_WEST(List.of(Direction.NORTH, Direction.WEST)), //exiting border
    SOUTH_WEST(List.of(Direction.SOUTH, Direction.WEST)), //exiting border
    SOUTH_EAST(List.of(Direction.SOUTH, Direction.EAST)), //entering border
    GROUND(List.of()),
    START(List.of());

    private static final Map<Character, NodeType> NODE_TYPE_BY_CHAR = Map.of(
            '|', NORTH_SOUTH,
            '-', EAST_WEST,
            'L', NORTH_EAST,
            'J', NORTH_WEST,
            '7', SOUTH_WEST,
            'F', SOUTH_EAST,
            '.', GROUND,
            'S', START
    );

    private static final Map<NodeType, Character> CHAR_BY_NODE_TYPE = Map.of(
            NORTH_SOUTH, '|',
            EAST_WEST, '-',
            NORTH_EAST, 'L',
            NORTH_WEST, 'J',
            SOUTH_WEST, '7',
            SOUTH_EAST, 'F',
            GROUND, '.',
            START, 'S'
            );

    /**
     * holds the possible directions of movement for the nodeType
     */
    private List<Direction> directions;

    private NodeType(List<Direction> directions) {
        this.directions = directions;
    }

    public Direction getDirectionTo(Direction directionFrom) {
        return this.directions.stream()
                .filter(direction -> direction != directionFrom)
                .findFirst()
                .get();
    }

    public boolean hasDirection(Direction direction) {
        return this.directions.stream()
                .anyMatch(direction1 -> direction1.equals(direction));
    }

    public static NodeType getNodeTypeFromChar(Character character) {
        return NODE_TYPE_BY_CHAR.get(character);
    }

    public Character getCharFromNodeType() {
        return CHAR_BY_NODE_TYPE.get(this);
    }

    /**
     * Returns through if passing this node will invert the loop state (inside or outside the loop)
     * @param previousCurve if there was a previous 90-degree curve before passing this node,
     *                     we should pass it to correctly determine if the loop state will be inverted or not
     * @return
     */
    public boolean invertsLoopState(NodeType previousCurve) {
        if (this == NORTH_SOUTH) {
            return true;
        }
        if (this == EAST_WEST || this == NORTH_EAST || this == SOUTH_EAST) {
            return false;
        }
        if (this == NORTH_WEST) {
            if (previousCurve == NORTH_EAST) {
                return false;
            }
            if (previousCurve == SOUTH_EAST) {
                return true;
            }
        }
        if (this == SOUTH_WEST) {
            if (previousCurve == NORTH_EAST) {
                return true;
            }
            if (previousCurve == SOUTH_EAST) {
                return false;
            }
        }
        return false;
    }
}
