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

        long result = getMaximumDistanceSloped(nodeByPosition.get(new Position(0, 1)), new HashSet<>(), nodeByPosition,
                nodeByPosition.get(new Position(rowIter - 1, numberOfColumns - 2)));
        result = result - 1;

        System.out.println("Maximum distance to exit is: " + result);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 23 Challenge 2: ");

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

        //create edges
        for(Node node : nodeByPosition.values()) {
            for(Position position : node.getNeighborPositions()) {
                if (nodeByPosition.get(position) != null) {
                    node.edges.add(new Edge(nodeByPosition.get(position), 1L));
                }
            }
        }

        //compact edges
        for(Node node : nodeByPosition.values()) {
            if(node.edges.size()==2) {
                Node firstNode = node.edges.get(0).edgeNode;
                Node secondNode = node.edges.get(1).edgeNode;
                //replace edges to this node
                for(Edge edge : firstNode.edges) {
                    if (edge.edgeNode == node) {
                        edge.edgeNode = secondNode;
                        edge.edgeDistance = edge.edgeDistance + node.edges.get(1).edgeDistance;
                    }
                }
                for(Edge edge : secondNode.edges) {
                    if (edge.edgeNode == node) {
                        edge.edgeNode = firstNode;
                        edge.edgeDistance = edge.edgeDistance + node.edges.get(0).edgeDistance;
                    }
                }
                node.edges.clear();
            }
        }

        Set<Node> compactedNodes = new HashSet<>();
        for(Node node : nodeByPosition.values()) {
            if (node.edges.isEmpty()) {
                continue;
            }
            compactedNodes.add(node);
        }

        long result = getMaximumDistance(nodeByPosition.get(new Position(0, 1)), new HashSet<>(),
                nodeByPosition.get(new Position(rowIter - 1, numberOfColumns - 2)));

        System.out.println("Maximum distance to exit is: " + result);
    }


    public static long getMaximumDistance(Node currentNode, Set<Node> visitedNodes, Node finalNode) {

        //get possible new nodes
        Set<Node> validNeighbors = new HashSet<>();
        for(Edge edge : currentNode.edges) {
            if(!visitedNodes.contains(edge.edgeNode)) {
                validNeighbors.add(edge.edgeNode);
            }
        }

        //handle terminal case of no validNeighbors
        if (validNeighbors.isEmpty()) {
            if(currentNode == finalNode){
                return 0;
            } else {
                return -1000000000;
            }
        }

        Set<Node> newVisitedNodes = new HashSet<>(visitedNodes);
        newVisitedNodes.add(currentNode);


        long maxDistance = 0;
        boolean isFirst = true;
        for(Node neighbor : validNeighbors) {
            long distance = getMaximumDistance(neighbor, newVisitedNodes, finalNode);
            for(Edge edge : neighbor.edges) {
                if(edge.edgeNode == currentNode) {
                    distance = distance + edge.edgeDistance;
                    break;
                }
            }
            if(distance > maxDistance || isFirst) {
                maxDistance = distance;
            }
            if (isFirst) {
                isFirst = false;
            }
        }
        return maxDistance;
    }

    public static long getMaximumDistanceSloped(Node currentNode, Set<Node> visitedNodes, Map<Position, Node> nodeByPosition, Node finalNode) {

        //get possible new nodes
        Set<Node> validNeighbors = new HashSet<>();
        for(Position position : currentNode.getNeighborPositions()) {
            if (nodeByPosition.get(position) != null && !visitedNodes.contains(nodeByPosition.get(position))
            && isAccessibleSlope(nodeByPosition, position, currentNode)) {
                validNeighbors.add(nodeByPosition.get(position));
            }
        }

        //handle terminal case of no validNeighbors
        if (validNeighbors.isEmpty()) {
            if(currentNode == finalNode){
                return 0;
            } else {
                return -1000000000;
            }
        }

        long tempDistance = 1;
        Set<Node> newVisitedNodes = new HashSet<>(visitedNodes);
        newVisitedNodes.add(currentNode);
        //handle case of only one neighbor
        if (validNeighbors.size() == 1) {
            Node newNode = null;
            while(validNeighbors.size() == 1) {
                tempDistance++;
                newNode = validNeighbors.stream().findFirst().get();
                newVisitedNodes.add(newNode);
                //get possible new nodes
                validNeighbors = new HashSet<>();
                for(Position position : newNode.getNeighborPositions()) {
                    if (nodeByPosition.get(position) != null && !newVisitedNodes.contains(nodeByPosition.get(position))
                            && isAccessibleSlope(nodeByPosition, position, newNode)) {
                        validNeighbors.add(nodeByPosition.get(position));
                    }
                }
            }
            //handle terminal case of no validNeighbors
            if (validNeighbors.isEmpty()) {
                if(newNode == finalNode){
                    return tempDistance;
                } else {
                    return -1000000000;
                }
            }
        }

        long maxDistance = 0;
        boolean isFirst = true;
        for(Node neighbor : validNeighbors) {
            long distance = getMaximumDistanceSloped(neighbor, newVisitedNodes, nodeByPosition, finalNode);
            if(distance > maxDistance || isFirst) {
                maxDistance = distance;
            }
            if (isFirst) {
                isFirst = false;
            }
        }
        return maxDistance + tempDistance;
    }

    private static boolean isAccessibleSlope(Map<Position, Node> nodeByPosition, Position position, Node node) {
        Node destinationNode = nodeByPosition.get(position);
        if(node.type == '.') {
            return true;
        }
        if(node.type == '>' && destinationNode.x == node.x && destinationNode.y == (node.y + 1)) {
            return true;
        }
        if(node.type == '<' && destinationNode.x == node.x && destinationNode.y == (node.y - 1)) {
            return true;
        }
        if(node.type == 'v' && destinationNode.x == (node.x + 1) && destinationNode.y == node.y) {
            return true;
        }
        return false;
    }
}