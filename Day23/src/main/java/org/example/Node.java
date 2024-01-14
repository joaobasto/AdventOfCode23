package org.example;

import java.util.ArrayList;
import java.util.List;

public class Node {

    long x;
    long y;
    char type;
    List<Edge> edges = new ArrayList<>();

    public Node(long x, long y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public List<Position> getNeighborPositions() {
        return List.of(new Position(x - 1, y), new Position(x + 1, y),
                new Position(x, y - 1), new Position(x, y + 1));
    }
}
