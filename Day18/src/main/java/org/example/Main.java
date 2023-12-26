package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final String initialNodeName = "AAA";
    private static final String finalNodeName = "ZZZ";

    public static void main(String[] args) throws IOException {
        solver(1);
        solver(2);
    }

    private static void solver(int exerciseNumber) throws IOException {
        System.out.println("Solving Day 8 Challenge " + exerciseNumber +": ");

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
            String[] lineData;
            Direction direction = null;
            long steps = 0;
            if (exerciseNumber == 1) {
                lineData = line.split(" ");
                direction = Direction.getDirectionFromString(lineData[0]);
                steps = Integer.parseInt(lineData[1]);
            } else if (exerciseNumber == 2) {
                lineData = line.split(" \\(#")[1].split("\\)");
                direction = Direction.getDirectionFromString(lineData[0].substring(lineData[0].length()-1));
                steps = Long.parseLong(lineData[0].substring(0, lineData[0].length()-1), 16);
            }
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
        long offsetX = nodes.stream()
                .map(Node::getX)
                .min(Comparator.comparingLong(a -> a))
                .get();
        long offsetY = nodes.stream()
                .map(Node::getY)
                .min(Comparator.comparingLong(a -> a))
                .get();
        nodes.forEach(node -> node.setX(node.getX() - offsetX));
        nodes.forEach(node -> node.setY(node.getY() - offsetY));

        long numberOfLines = nodes.stream()
                .map(Node::getX)
                .max(Comparator.comparingLong(a -> a))
                .get() + 1;
        long numberOfColumns = nodes.stream()
                .map(Node::getY)
                .max(Comparator.comparingLong(a -> a))
                .get() + 1;

        //set curve type for each node
        for(Node node : nodes) {
            node.determineCurveType();
        }

        //generate map for nodes
        TreeMap<Long, TreeMap<Long, Node>> nodeMap = new TreeMap<>();
        for(Node node : nodes) {
            nodeMap.computeIfAbsent(node.getX(), unused -> new TreeMap<>());
            nodeMap.get(node.getX()).put(node.getY(), node);
        }

        //Now, we basically have to solve something similar to Day10, where we
        //checked the number of positions inside the loop
        //But we have to improve the performance for this one. So, we do the following:
        //The tiles inside the loop and in the border for a given row only change if
        //that row has nodes. So, we use the node tree map to quickly navigate to nodes,
        //and in between node-changing rows, we just need to multiply the tiles in the starting row range
        //by the number of rows in that range
        long totalDiffY = 0;
        long rowIndex = 0;
        long previousRowIndex = 0;
        long result = 0;
        long squaresInsideLoop = 0;
        long squaresInBorder = 0;
        TreeMap<Long, NodeStatus> currentNodesByColumnIndex = new TreeMap<>();
        for(Map.Entry<Long, TreeMap<Long, Node>> entry : nodeMap.entrySet()) {

            long auxBorder = 0;
            long auxInside = 0;
            boolean inside = false;
            NodeStatus previousNodeStatus = null;
            for(Map.Entry<Long, NodeStatus> entry2 : currentNodesByColumnIndex.entrySet()) {
                if (entry2.getValue().isInCurrentRow()) {
                    continue;
                }
                NodeStatus nodeStatus = entry2.getValue();
                auxBorder++;
                inside = !inside;
                if (!inside) {
                    auxInside += nodeStatus.getNode().getY() - previousNodeStatus.getNode().getY() - 1;
                }
                previousNodeStatus = nodeStatus;
            }
            rowIndex = entry.getKey();
            result += ((auxInside + auxBorder) *  (rowIndex - previousRowIndex - 1));
            totalDiffY = 0;
            previousRowIndex = rowIndex;

            for(Map.Entry<Long, Node> entry1 : entry.getValue().entrySet()) {
                currentNodesByColumnIndex.put(entry1.getKey(), new NodeStatus(entry1.getValue(), true));
            }

            List<NodeStatus> rowNodeStatus = currentNodesByColumnIndex.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            boolean isInsideLoop = false;
            boolean isInBorder = false;
            CurveType previousCurve = null;
            Node previousStartNode = null;
            for(NodeStatus nodeStatus : rowNodeStatus) {
                if (!nodeStatus.isInCurrentRow()) {
                    isInsideLoop = !isInsideLoop;
                    if (!isInsideLoop) {
                        squaresInsideLoop += nodeStatus.getNode().getY() - previousStartNode.getY() - 1;
                        squaresInBorder++;
                    }
                    if (isInsideLoop) {
                        previousStartNode = nodeStatus.getNode();
                        squaresInBorder++;
                    }
                    previousCurve = CurveType.VERTICAL;
                    previousStartNode = nodeStatus.getNode();
                    continue;
                }
                //get node type
                CurveType curveType = nodeStatus.getNode().getCurveType();
                if (isInBorder && curveType.exitsBorder()) {
                    squaresInBorder += nodeStatus.getNode().getY() - previousStartNode.getY() + 1;
                }
                if (!isInBorder && curveType.entersBorder() && isInsideLoop) {
                    squaresInsideLoop += nodeStatus.getNode().getY() - previousStartNode.getY() - 1;
                }
                isInBorder = (isInBorder || curveType.entersBorder()) && !curveType.exitsBorder();
                if (curveType.invertsStateRow(previousCurve)) {
                    isInsideLoop = !isInsideLoop;
                    if (!isInsideLoop) {
                        totalDiffY += nodeStatus.getNode().getY() - previousStartNode.getY() + 1;
                    }
                }
                previousCurve = curveType;
                previousStartNode = nodeStatus.getNode();
            }
            for(Iterator<Map.Entry<Long, NodeStatus>> it = currentNodesByColumnIndex.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Long, NodeStatus> entry1 = it.next();
                if(entry1.getValue().getNode().getCurveType() == CurveType.UP_LEFT ||
                        entry1.getValue().getNode().getCurveType() == CurveType.UP_RIGHT) {
                    it.remove();
                } else {
                    entry1.getValue().setInCurrentRow(false);
                }
            }
        }
        result = result + squaresInBorder + squaresInsideLoop;

        System.out.println("Total available cubic meters are: " + (result));
    }
}