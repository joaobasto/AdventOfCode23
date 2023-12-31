package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 21 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        Map<Position, Node> nodeByPosition = new HashMap<>();
        Node startNode = null;
        String line;
        int lineIndex = 0;
        while ((line = br.readLine()) != null) {
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '.') {
                    nodeByPosition.put(new Position(lineIndex, i), new Node(lineIndex, i));
                }
                if (chars[i] == 'S') {
                    nodeByPosition.put(new Position(lineIndex, i), new Node(lineIndex, i, 0));
                    startNode = nodeByPosition.get(new Position(lineIndex, i));
                }
            }
            lineIndex++;
        }

        //now we must create the connections between the nodes
        for(Node node : nodeByPosition.values()) {
            //north
            if (nodeByPosition.get(new Position(node.getX() - 1, node.getY())) != null) {
                node.addNeighbor(nodeByPosition.get(new Position(node.getX() - 1, node.getY())));
            }
            //south
            if (nodeByPosition.get(new Position(node.getX() + 1, node.getY())) != null) {
                node.addNeighbor(nodeByPosition.get(new Position(node.getX() + 1, node.getY())));
            }
            //east
            if (nodeByPosition.get(new Position(node.getX(), node.getY() + 1)) != null) {
                node.addNeighbor(nodeByPosition.get(new Position(node.getX(), node.getY() + 1)));
            }
            //west
            if (nodeByPosition.get(new Position(node.getX(), node.getY() - 1)) != null) {
                node.addNeighbor(nodeByPosition.get(new Position(node.getX(), node.getY() - 1)));
            }
        }

        //Dijsktra algorithm
        long validNodeCount = 0;
        Set<Node> validSet = new HashSet<>();
        Comparator<Node> nodeComparator = new Comparator<Node>()
        {
            @Override
            public int compare(Node n1, Node n2)
            {
                return n1.getTentativeDistance() - n2.getTentativeDistance();
            }
        };
        PriorityQueue<Node> unvisitedQueue = new PriorityQueue<>(nodeComparator);
        unvisitedQueue.addAll(nodeByPosition.values());
        while (true) {
            Node currentNode = unvisitedQueue.poll();
            if (currentNode.getTentativeDistance() > 64) {
                break;
            }
            Set<Node> changedDistanceNodes = new HashSet<>();
            for (Node neighborNode : currentNode.getNeighbors()) {
                if (unvisitedQueue.contains(neighborNode)) {
                    int newTentativeDistance = currentNode.getTentativeDistance() + 1;
                    if (newTentativeDistance < neighborNode.getTentativeDistance()) {
                        neighborNode.setTentativeDistance(newTentativeDistance);
                        changedDistanceNodes.add(neighborNode);
                    }
                }
            }
            //remove and insert changed distance nodes to keep them in a sorted position in the tree set
            for (Node node : changedDistanceNodes) {
                unvisitedQueue.remove(node);
                unvisitedQueue.add(node);
            }
            validNodeCount++;
            validSet.add(currentNode);
            if (unvisitedQueue.isEmpty()) {
                break;
            }
        }

        //nodes that are in an odd distance are impossible to reach at an even ammount of steps
        //so we remove them from the count
        for(Node node : validSet) {
            if (node.getTentativeDistance() % 2 == 1) {
                validNodeCount--;
            }
        }

        System.out.println("Valid node count is: " + validNodeCount);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 21 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {

        }
    }


}