package org.example;

import java.util.Objects;

public class State {

    private Position position;

    private Direction direction;

    private int consecutiveSteps;

    public State(Position position, Direction direction, int consecutiveSteps) {
        this.position = position;
        this.direction = direction;
        this.consecutiveSteps = consecutiveSteps;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getConsecutiveSteps() {
        return consecutiveSteps;
    }

    public void setConsecutiveSteps(int consecutiveSteps) {
        this.consecutiveSteps = consecutiveSteps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return consecutiveSteps == state.consecutiveSteps && Objects.equals(position, state.position) && direction == state.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, direction, consecutiveSteps);
    }
}
