package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.Direction.*;

public class Node {

    private int x;
    private int y;
    private CurveType curveType;
    private List<Node> connectedNodes;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        connectedNodes = new ArrayList<>();
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

    public CurveType getCurveType() {
        return curveType;
    }

    public void setCurveType(CurveType curveType) {
        this.curveType = curveType;
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public void setConnectedNodes(List<Node> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Node getNodeAfterMove(Direction direction, int numberOfSteps) {
        if(direction == LEFT) {
            return new Node(this.getX(), this.getY() - numberOfSteps);
        }
        if(direction == RIGHT) {
            return new Node(this.getX(), this.getY() + numberOfSteps);
        }
        if(direction == UP) {
            return new Node(this.getX() - numberOfSteps, this.getY());
        }
        if(direction == DOWN) {
            return new Node(this.getX() + numberOfSteps, this.getY());
        }
        return null;
    }

    public void determineCurveType() {
        //check direction of each node
        Direction direction1 = getDirectionToNode(this.getConnectedNodes().get(0));
        Direction direction2 = getDirectionToNode(this.getConnectedNodes().get(1));
        CurveType curveType = determineCurveType(List.of(direction1, direction2));
        this.setCurveType(curveType);
    }

    private CurveType determineCurveType(List<Direction> directions) {
        if(directions.containsAll(List.of(UP, LEFT))) {
            return CurveType.UP_LEFT;
        }
        if(directions.containsAll(List.of(UP, RIGHT))) {
            return CurveType.UP_RIGHT;
        }
        if(directions.containsAll(List.of(DOWN, LEFT))) {
            return CurveType.DOWN_LEFT;
        }
        if(directions.containsAll(List.of(DOWN, RIGHT))) {
            return CurveType.DOWN_RIGHT;
        }
        return null;
    }


    private Direction getDirectionToNode(Node node) {
        if(this.getX() == node.getX()) {
            if(this.getY() < node.getY()) {
                return RIGHT;
            }
            else {
                return LEFT;
            }
        } else {
            if(this.getX() < node.getX()) {
                return DOWN;
            }
            else {
                return UP;
            }
        }
    }
}
