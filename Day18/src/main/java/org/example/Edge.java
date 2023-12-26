package org.example;

import java.util.Objects;

public class Edge {

    private Node node1;
    private Node node2;

    public Edge(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public long getMinX() {
        return Math.min(node1.getX(), node2.getX());
    }

    public long getMinY() {
        return Math.min(node1.getY(), node2.getY());
    }

    public long getMaxX() {
        return Math.max(node1.getX(), node2.getX());
    }

    public long getMaxY() {
        return Math.max(node1.getY(), node2.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (Objects.equals(node1, edge.node1) && Objects.equals(node2, edge.node2)) ||
                (Objects.equals(node1, edge.node2) && Objects.equals(node2, edge.node1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(node1, node2) + Objects.hash(node2, node1);
    }
}
