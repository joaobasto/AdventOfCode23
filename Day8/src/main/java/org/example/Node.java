package org.example;

import java.util.Objects;

public class Node {

    private final String name;
    private Node leftChild;
    private Node rightChild;

    private NodeMovementData nodeMovementData;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public NodeMovementData getNodeMovementData() {
        return nodeMovementData;
    }

    public void setNodeMovementData(NodeMovementData nodeMovementData) {
        this.nodeMovementData = nodeMovementData;
    }

    public Long getCurrentPosition() {
        return this.nodeMovementData.getCurrentPosition();
    }

    public void getNextFinalPositionStep() {
        this.nodeMovementData.getNextFinalPositionStep();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
