package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

    private int x;
    private int y;

    private int tentativeDistance;

    private List<Node> neighbors;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.tentativeDistance = Integer.MAX_VALUE;
        this.neighbors = new ArrayList<>();
    }

    public Node(int x, int y, int distance) {
        this.x = x;
        this.y = y;
        this.tentativeDistance = distance;
        this.neighbors = new ArrayList<>();
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

    public int getTentativeDistance() {
        return tentativeDistance;
    }

    public void setTentativeDistance(int tentativeDistance) {
        this.tentativeDistance = tentativeDistance;
    }

    public void addNeighbor(Node neighborNode) {
        this.neighbors.add(neighborNode);
    }

    public List<Node> getNeighbors() {
        return neighbors;
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
}
