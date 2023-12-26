package org.example;

public class NodeStatus {

    private Node node;
    private boolean isInCurrentRow;

    public NodeStatus(Node node, boolean isInCurrentRow) {
        this.node = node;
        this.isInCurrentRow = isInCurrentRow;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isInCurrentRow() {
        return isInCurrentRow;
    }

    public void setInCurrentRow(boolean inCurrentRow) {
        isInCurrentRow = inCurrentRow;
    }
}
