package org.example;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 23 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));


        //read the nodes into a map
        Map<Position, Node> nodeByPosition = new HashMap<>();
        long rowIter = 0;
        String line;
        long numberOfColumns = 0;
        while ((line = br.readLine()) != null) {
            char[] chars = line.toCharArray();
            numberOfColumns = chars.length;
            for (int i = 0; i < chars.length; i++) {
                if (!(chars[i] == '#')){
                    nodeByPosition.put(new Position(rowIter, i), new Node(rowIter, i, chars[i]));
                }
            }
            rowIter++;
        }

        Position startPosition = new Position(0, 1);
        Node initialNode = nodeByPosition.get(startPosition);
        Set<Node> visitedNodes = new HashSet<>();
        List<Node> toVisitNodes = new ArrayList<>();
        toVisitNodes.add(initialNode);

        while(!toVisitNodes.isEmpty()) {
            //take the last node to visit to assure BFS
            Node node = toVisitNodes.get(toVisitNodes.size() - 1);
            toVisitNodes.remove(node);
            //check possible successor nodes and if they were not a predecessor
            for(Position position : node.getNeighborPositions()) {
                if (nodeByPosition.get(position) != null && !node.predecessorNodes.contains(nodeByPosition.get(position))
                && isAccessible(nodeByPosition, position, node)) {
                    //add them to list of successors and this node to their predecessors
                    node.successorNodes.add(nodeByPosition.get(position));
                    nodeByPosition.get(position).predecessorNodes.add(node);
                    //add them to the toVisitNodes if they were not visited yet
                    if (!visitedNodes.contains(nodeByPosition.get(position)) &&
                    !toVisitNodes.contains(nodeByPosition.get(position))) {
                        toVisitNodes.add(nodeByPosition.get(position));
                    }
                }
            }
            visitedNodes.add(node);
        }

        visitedNodes.clear();
        toVisitNodes.clear();
        toVisitNodes.add(initialNode);
        while(!toVisitNodes.isEmpty()) {
            //take the last node
            Node node = toVisitNodes.get(toVisitNodes.size() - 1);
            toVisitNodes.remove(node);
            //update distance and add to visited
            if (node != initialNode) {
                node.distance = node.predecessorNodes.stream().map(node2 -> node2.distance).max(Long::compareTo).get() + 1;
            }
            visitedNodes.add(node);
            for(Node sucessorNode : node.successorNodes) {
                if(visitedNodes.containsAll(sucessorNode.predecessorNodes) && !toVisitNodes.contains(sucessorNode)) {
                    toVisitNodes.add(sucessorNode);
                }
            }
        }

        Position finalPosition = new Position(rowIter - 1, numberOfColumns - 2);
        System.out.println("Maximum distance to exit is: " + nodeByPosition.get(finalPosition).distance);
    }

    private static boolean isAccessible(Map<Position, Node> nodeByPosition, Position position, Node node) {
        Node destinationNode = nodeByPosition.get(position);
        if(node.type == '>' && destinationNode.x == node.x && destinationNode.y == (node.y + 1)) {
            return true;
        }
        if(node.type == '<' && destinationNode.x == node.x && destinationNode.y == (node.y - 1)) {
            return true;
        }
        if(node.type == 'v' && destinationNode.x == (node.x + 1) && destinationNode.y == node.y) {
            return true;
        }
        if(destinationNode.type == '>' && destinationNode.x == node.x && destinationNode.y == (node.y - 1)) {
            return false;
        }
        if(destinationNode.type == '<' && destinationNode.x == node.x && destinationNode.y == (node.y + 1)) {
            return false;
        }
        if(destinationNode.type == 'v' && destinationNode.x == (node.x - 1) && destinationNode.y == node.y) {
            return false;
        }
        return true;
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 23 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
        }
    }
}