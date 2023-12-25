package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final String initialNodeName = "AAA";
    private static final String finalNodeName = "ZZZ";

    public static void main(String[] args) throws IOException {
        exercise1();
        //exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 18 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        Set<Node> nodes = new HashSet<>();
        Set<Edge> horizontalEdges = new HashSet<>();
        Set<Edge> verticalEdges = new HashSet<>();
        Node initialNode = new Node(0, 0);
        Node currentNode = initialNode;
        nodes.add(currentNode);
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" ");
            Direction direction = Direction.getDirectionFromString(lineData[0]);
            int steps = Integer.parseInt(lineData[1]);
            Node newNode = currentNode.getNodeAfterMove(direction, steps);
            nodes.add(newNode);
            if(newNode.getX() == 0 && newNode.getY() == 0) {
                newNode = initialNode;
            }
            if (direction == Direction.UP || direction == Direction.DOWN) {
                verticalEdges.add(new Edge(currentNode, newNode));
            } else {
                horizontalEdges.add(new Edge(currentNode, newNode));
            }
            currentNode.getConnectedNodes().add(newNode);
            newNode.getConnectedNodes().add(currentNode);
            currentNode = newNode;
        }

        //offset correction
        int offsetX = nodes.stream()
                .map(Node::getX)
                .min(Comparator.comparingInt(a -> a))
                .get();
        int offsetY = nodes.stream()
                .map(Node::getY)
                .min(Comparator.comparingInt(a -> a))
                .get();
        nodes.forEach(node -> node.setX(node.getX() - offsetX));
        nodes.forEach(node -> node.setY(node.getY() - offsetY));

        int numberOfLines = nodes.stream()
                .map(Node::getX)
                .max(Comparator.comparingInt(a -> a))
                .get() + 1;
        int numberOfColumns = nodes.stream()
                .map(Node::getY)
                .max(Comparator.comparingInt(a -> a))
                .get() + 1;
        List<List<Character>> map = new ArrayList<>();
        for(int i = 0; i < numberOfLines; i++) {
            map.add(new ArrayList<>());
            for(int j = 0; j < numberOfColumns; j++) {
                map.get(i).add('.');
            }
        }
        for(Edge edge : horizontalEdges) {
            int x = edge.getMaxX();
            int minY = edge.getMinY();
            int maxY = edge.getMaxY();
            for(int i = minY; i <= maxY; i++) {
                map.get(x).set(i, '#');
            }
        }
        for(Edge edge : verticalEdges) {
            int y = edge.getMaxY();
            int minX = edge.getMinX();
            int maxX = edge.getMaxX();
            for(int i = minX; i <= maxX; i++) {
                map.get(i).set(y, '#');
            }
        }

        //Printing map to check if it looks correct
        printMap(map);

        //set curve type for each node
        for(Node node : nodes) {
            node.determineCurveType();
        }

        //generate map for nodes
        Map<Integer, Map<Integer, Node>> nodeMap = new HashMap<>();
        for(Node node : nodes) {
            nodeMap.computeIfAbsent(node.getX(), unused -> new HashMap<>());
            nodeMap.get(node.getX()).put(node.getY(), node);
        }

        //Now, we basically have to solve something similar to Day10, where we
        //checked the number of positions inside the loop
        int tilesInLoop = 0;
        int tilesInBorder = 0;
        for (int i = 0; i < map.size(); i++) {
            boolean isInsideLoop = false;
            CurveType previousCurve = null;
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j).equals('#')) {
                    tilesInBorder++;
                }
                Node node = null;
                CurveType curveType;
                //check if it is a node and get curveType
                if (nodeMap.get(i) != null && nodeMap.get(i).get(j) != null) {
                    node = nodeMap.get(i).get(j);
                    //get node type
                    curveType = node.getCurveType();
                    if (curveType.invertsState(previousCurve)) {
                        isInsideLoop = !isInsideLoop;
                    }
                    previousCurve = curveType;
                } else if (map.get(i).get(j).equals('.') &&
                        j > 0 && map.get(i).get(j-1).equals('#')
                && (nodeMap.get(i) == null || nodeMap.get(i).get(j - 1) == null)) {
                    isInsideLoop = !isInsideLoop;
                    if(isInsideLoop) {
                        tilesInLoop++;
                    }
                } else if(isInsideLoop && map.get(i).get(j).equals('.')) {
                    tilesInLoop++;
                }
            }
        }

        System.out.println("Total available cubic meters are: " + (tilesInBorder + tilesInLoop));
    }

    private static void printMap(List<List<Character>> map) {
        for(List<Character> characters: map) {
            StringBuilder builder = new StringBuilder(characters.size());
            for(Character ch: characters)
            {
                builder.append(ch);
            }
            System.out.println(builder);
        }
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 8 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" = \\(");
        }
    }
}