package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final String initialNodeName = "AAA";
    private static final String finalNodeName = "ZZZ";

    public static void main(String[] args) throws IOException {
        //exercise1();
        //exercise2v2();
        exercise2v3();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 8 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        char[] steps = br.readLine().toCharArray();
        br.readLine();

        Map<String, Node> nodesByName = new HashMap<>();
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" = \\(");
            String nodeName = lineData[0];
            String[] childrenData = lineData[1].split(", |\\)");
            String leftChildName = childrenData[0];
            String rightChildName = childrenData[1];
            nodesByName.computeIfAbsent(nodeName, Node::new);
            Node leftChildNode = nodesByName.computeIfAbsent(leftChildName, Node::new);
            Node rightChildNode = nodesByName.computeIfAbsent(rightChildName, Node::new);
            nodesByName.get(nodeName).setLeftChild(leftChildNode);
            nodesByName.get(nodeName).setRightChild(rightChildNode);
        }

        Node currentNode = nodesByName.get(initialNodeName);
        long totalSteps = 0;
        int stepArrayIndex = 0;
        while (true) {
            if (steps[stepArrayIndex] == 'L') {
                currentNode = currentNode.getLeftChild();
            } else {
                currentNode = currentNode.getRightChild();
            }
            totalSteps++;
            if (currentNode.getName().equals(finalNodeName)) {
                break;
            }
            stepArrayIndex++;
            if (stepArrayIndex == steps.length) {
                stepArrayIndex = 0;
            }
        }

        System.out.println("Total number of steps to reach final node is: " + totalSteps);
    }

    // Naive solution for exercise 2:
    // Traverses all the paths until they reach final nodes at the same time
    private static void exercise2() throws IOException {
        System.out.println("Solving Day 8 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        char[] steps = br.readLine().toCharArray();
        br.readLine();

        Map<String, Node> nodesByName = new HashMap<>();
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" = \\(");
            String nodeName = lineData[0];
            String[] childrenData = lineData[1].split(", |\\)");
            String leftChildName = childrenData[0];
            String rightChildName = childrenData[1];
            nodesByName.computeIfAbsent(nodeName, Node::new);
            Node leftChildNode = nodesByName.computeIfAbsent(leftChildName, Node::new);
            Node rightChildNode = nodesByName.computeIfAbsent(rightChildName, Node::new);
            nodesByName.get(nodeName).setLeftChild(leftChildNode);
            nodesByName.get(nodeName).setRightChild(rightChildNode);
        }

        List<Node> currentNodes = nodesByName.values().stream()
                .filter(node -> node.getName().endsWith("A"))
                .collect(Collectors.toList());
        long totalSteps = 0;
        int stepArrayIndex = 0;
        while (true) {
            if (steps[stepArrayIndex] == 'L') {
                currentNodes = currentNodes.stream()
                        .map(Node::getLeftChild)
                        .collect(Collectors.toList());
            } else {
                currentNodes = currentNodes.stream()
                        .map(Node::getRightChild)
                        .collect(Collectors.toList());
            }
            totalSteps++;
            boolean isFinalPosition = currentNodes.stream()
                    .map(Node::getName)
                    .allMatch(name -> name.endsWith("Z"));
            if (isFinalPosition) {
                break;
            }
            stepArrayIndex++;
            if (stepArrayIndex == steps.length) {
                stepArrayIndex = 0;
            }
            System.out.println(totalSteps);
        }

        System.out.println("Total number of steps to reach final node is: " + totalSteps);
    }

    // Optimized solution for exercise 2:
    // After understanding that the first solution was taking too long,
    // I understood probably the idea would be to check for infinite traversal loops,
    // get the steps when the final nodes are reached in these loops, and then
    // we can advance to the next steps by simply taking the steps of the current loop and
    // adding them the size of the loop.
    // This avoids traversal of all the nodes, and basically this solution uses this to
    // travel through the steps faster and obtain the solution in less time.
    // Still, the solution takes too long to be calculated.
    // After realizing some details of the input data, I created a third solution method
    // (more details in the comment above its method), but keep in mind that it only works
    // for the specific cases described there.
    // For a general input data for this problem, this is the method that should be applied.
    private static void exercise2v2() throws IOException {
        System.out.println("Solving Day 8 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        char[] steps = br.readLine().toCharArray();
        br.readLine();

        Map<String, Node> nodesByName = new HashMap<>();
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" = \\(");
            String nodeName = lineData[0];
            String[] childrenData = lineData[1].split(", |\\)");
            String leftChildName = childrenData[0];
            String rightChildName = childrenData[1];
            nodesByName.computeIfAbsent(nodeName, Node::new);
            Node leftChildNode = nodesByName.computeIfAbsent(leftChildName, Node::new);
            Node rightChildNode = nodesByName.computeIfAbsent(rightChildName, Node::new);
            nodesByName.get(nodeName).setLeftChild(leftChildNode);
            nodesByName.get(nodeName).setRightChild(rightChildNode);
        }

        List<Node> initialNodes = nodesByName.values().stream()
                .filter(node -> node.getName().endsWith("A"))
                .collect(Collectors.toList());
        long totalSteps = 0;
        int stepArrayIndex = 0;
        //calculate repetition cycle for each initial node
        Map<Node, Map<Integer, Long>> stepsByArrayIndexByNode = new HashMap<>();
        List<Long> stepsInFinalPosition;
        for(Node initialNode : initialNodes) {
            stepsInFinalPosition = new ArrayList<>();
            Node currentNode = initialNode;
            totalSteps = 0;
            stepArrayIndex = 0;
            while (true) {
                if (steps[stepArrayIndex] == 'L') {
                    currentNode = currentNode.getLeftChild();
                } else {
                    currentNode = currentNode.getRightChild();
                }
                totalSteps++;
                stepsByArrayIndexByNode.computeIfAbsent(currentNode, unused -> new HashMap<>());
                if (stepsByArrayIndexByNode.get(currentNode).get(stepArrayIndex) == null) {
                    stepsByArrayIndexByNode.get(currentNode).put(stepArrayIndex, totalSteps);
                } else {
                    //the movement completed a loop
                    //we add the node movement data to the initial node
                    initialNode.setNodeMovementData(
                            new NodeMovementData(
                                    stepsInFinalPosition,
                                    totalSteps - stepsByArrayIndexByNode.get(currentNode).get(stepArrayIndex),
                                    stepsByArrayIndexByNode.get(currentNode).get(stepArrayIndex)));
                    break;
                }
                if(currentNode.getName().endsWith("Z")) {
                    stepsInFinalPosition.add(totalSteps);
                }
                stepArrayIndex++;
                if (stepArrayIndex == steps.length) {
                    stepArrayIndex = 0;
                }
            }
        }

        initialNodes.forEach(Node::getNextFinalPositionStep);
        Long result;
        while (true) {
            Long minPosition = initialNodes.get(0).getCurrentPosition();
            Node minNode = initialNodes.get(0);
            boolean allInSamePosition = true;
            for(int i = 0; i < initialNodes.size(); i++) {
                if(i > 0 && !initialNodes.get(i).getCurrentPosition().equals(initialNodes.get(i-1).getCurrentPosition())) {
                    allInSamePosition = false;
                }
                if (minPosition > initialNodes.get(i).getCurrentPosition()) {
                    minNode = initialNodes.get(i);
                    minPosition = initialNodes.get(i).getCurrentPosition();
                }
            }
            if (allInSamePosition) {
                result = minPosition;
                break;
            }
            minNode.getNextFinalPositionStep();
        }

        System.out.println("Total number of steps to reach final node is: " + result);
    }

    // Optimized solution for exercise 2:
    // I observed 2 important characteristics of the input data:
    // 1. For all the paths created from the initial nodes, the repetitive traversal loop
    // they have only passes 1 time in 1 final solution
    // 2. For all the paths created from the initial nodes, the repetitive traversal loop
    // has a loop size equal to the sum of initial position where the loop starts with the
    // number of steps inside the loop it takes to reach the final solution. Or, in other
    // words, the number of steps to reach the final solution from the start is equal to the
    // number of steps to reach the final solution from the final solution itself.
    // This means that for all the initial nodes, we will reach the final solution every N steps,
    // where N is its repetitive traversal loop size.
    // So we will reach the final node at the same step for all the nodes in a step that is
    // multiple of all the loop sizes. Given that we want the minimum amount of steps,
    // we calculate the least common multiple.
    // P.S.: I am disappointed with the fact that this problem requires a solution
    // that is "tailor-made" for the specificities of this input data. I always aimed to
    // create solutions that would work with any input data, as long as it followed the
    // characteristics defined in the problem statement. Nowhere in the problem statement,
    // the specific characteristics of the input data of this problem that allowed the usage
    // of this solution are mentioned. So although this solution was the one used to obtain the
    // answer for the Advent of Code problem, please consider the solution method in exercise2v2
    // as the correct one.
    private static void exercise2v3() throws IOException {
        System.out.println("Solving Day 8 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        char[] steps = br.readLine().toCharArray();
        br.readLine();

        Map<String, Node> nodesByName = new HashMap<>();
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" = \\(");
            String nodeName = lineData[0];
            String[] childrenData = lineData[1].split(", |\\)");
            String leftChildName = childrenData[0];
            String rightChildName = childrenData[1];
            nodesByName.computeIfAbsent(nodeName, Node::new);
            Node leftChildNode = nodesByName.computeIfAbsent(leftChildName, Node::new);
            Node rightChildNode = nodesByName.computeIfAbsent(rightChildName, Node::new);
            nodesByName.get(nodeName).setLeftChild(leftChildNode);
            nodesByName.get(nodeName).setRightChild(rightChildNode);
        }

        List<Node> initialNodes = nodesByName.values().stream()
                .filter(node -> node.getName().endsWith("A"))
                .collect(Collectors.toList());
        long totalSteps = 0;
        int stepArrayIndex = 0;
        //calculate repetition cycle for each initial node
        Map<Node, Map<Integer, Long>> stepsByArrayIndexByNode = new HashMap<>();
        List<Long> stepsInFinalPosition;
        for(Node initialNode : initialNodes) {
            stepsInFinalPosition = new ArrayList<>();
            Node currentNode = initialNode;
            totalSteps = 0;
            stepArrayIndex = 0;
            while (true) {
                if (steps[stepArrayIndex] == 'L') {
                    currentNode = currentNode.getLeftChild();
                } else {
                    currentNode = currentNode.getRightChild();
                }
                totalSteps++;
                stepsByArrayIndexByNode.computeIfAbsent(currentNode, unused -> new HashMap<>());
                if (stepsByArrayIndexByNode.get(currentNode).get(stepArrayIndex) == null) {
                    stepsByArrayIndexByNode.get(currentNode).put(stepArrayIndex, totalSteps);
                } else {
                    //the movement completed a loop
                    //we add the node movement data to the initial node
                    initialNode.setNodeMovementData(
                            new NodeMovementData(
                                    stepsInFinalPosition,
                                    totalSteps - stepsByArrayIndexByNode.get(currentNode).get(stepArrayIndex),
                                    stepsByArrayIndexByNode.get(currentNode).get(stepArrayIndex)));
                    break;
                }
                if(currentNode.getName().endsWith("Z")) {
                    stepsInFinalPosition.add(totalSteps);
                }
                stepArrayIndex++;
                if (stepArrayIndex == steps.length) {
                    stepArrayIndex = 0;
                }
            }
        }

        //calculate least common multiple
        Long max = initialNodes.stream().mapToLong(node -> node.getNodeMovementData().loopSize).max().getAsLong();
        Long lcm = max;
        while (true) {
            Long temp_lcm = lcm;
            if (initialNodes.stream().noneMatch(node -> temp_lcm % node.getNodeMovementData().loopSize != 0)) {
                break;
            }
            lcm += max;
        }

        System.out.println("Total number of steps to reach final node is: " + lcm);
    }
}