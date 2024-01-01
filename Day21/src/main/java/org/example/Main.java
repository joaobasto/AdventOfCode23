package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2(26501365);
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
                        changedDistanceNodes.add(neighborNode);
                    }
                }
            }
            //remove and insert changed distance nodes to keep them in a sorted position in the tree set
            for (Node node : changedDistanceNodes) {
                unvisitedQueue.remove(node);
                node.setTentativeDistance(currentNode.getTentativeDistance() + 1);
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

    //For these problems I was able to observe the following things while experimenting with the data:
    //The starting position is in the center of the map
    //The corners of the map are all at the distance of 130, given that the map is 131*131, this means that
    //this is the Manhattan distance, so there are no obstacles forcing to go around them
    //The map is surround in all sides by columns/rows without obstacles
    //This means that for a repeating of the map not in the horizontal axis or vertical axis we can calculate the
    //distance of its nodes by the distance of each node to the corner of the direction it is from the start node
    //and then sum the offset of 131 multiplied by the number of maps that appeared.
    //Another important observation: from the start node, for all the directions the path is clear
    //So we can apply the same rationale for the map repetitions in the horizontal and vertical axis
    //So, I will just repeat the map in order to create a 3*3 matrix of maps to calculate the distance of the nodes
    //Then I will sum the distance for each node of each of the repeated maps the number of times it would fit under
    //the maximum number of steps.
    private static void exercise2(int maxSteps) throws IOException {
        System.out.println("Solving Day 21 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        Map<Position, Node> nodeByPosition = new HashMap<>();
        Node startNode = null;
        String line;
        int lineIndex = 0;
        List<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String line2 = new String(new char[1]).replace("\0", line).replace('S', '.');
            line = line2.concat(line).concat(line2);
            lines.add(line);
        }

        List<String> completeLines = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < lines.size(); j++) {
                String text = lines.get(j);
                if(text.contains("S") && i !=1) {
                    text = text.replace('S', '.');
                }
                completeLines.add(text);
            }
        }

        for(String line2 : completeLines) {
            char[] chars = line2.toCharArray();
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
            if (currentNode.getTentativeDistance() == maxSteps) {
                break;
            }
            Set<Node> changedDistanceNodes = new HashSet<>();
            for (Node neighborNode : currentNode.getNeighbors()) {
                if (unvisitedQueue.contains(neighborNode)) {
                    int newTentativeDistance = currentNode.getTentativeDistance() + 1;
                    if (newTentativeDistance < neighborNode.getTentativeDistance()) {
                        changedDistanceNodes.add(neighborNode);
                    }
                }
            }
            //remove and insert changed distance nodes to keep them in a sorted position in the tree set
            for (Node node : changedDistanceNodes) {
                unvisitedQueue.remove(node);
                node.setTentativeDistance(currentNode.getTentativeDistance() + 1);
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
            if (node.getTentativeDistance() % 2 == (maxSteps%2 == 0 ? 1: 0)) {
                validNodeCount--;
            }
        }

        //TODO check up-left
        long countNodes = validNodeCount;
        for(int i = 0*131; i < 0*131 + 131; i++) {
            for(int j = 0*131; j < 0*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }
        //TODO check down-left
        for(int i = 2*131; i < 2*131 + 131; i++) {
            for(int j = 0*131; j < 0*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }
        //TODO check up-right
        for(int i = 0*131; i < 0*131 + 131; i++) {
            for(int j = 2*131; j < 2*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }
        //TODO check down-right
        for(int i = 2*131; i < 2*131 + 131; i++) {
            for(int j = 2*131; j < 2*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }
        //TODO check up
        for(int i = 0*131; i < 0*131 + 131; i++) {
            for(int j = 1*131; j < 1*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }
        //TODO check down
        for(int i = 2*131; i < 2*131 + 131; i++) {
            for(int j = 1*131; j < 1*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }
        //TODO check left
        for(int i = 1*131; i < 1*131 + 131; i++) {
            for(int j = 0*131; j < 0*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }
        //TODO check right
        for(int i = 1*131; i < 1*131 + 131; i++) {
            for(int j = 2*131; j < 2*131 + 131; j++) {
                if(nodeByPosition.get(new Position(i, j)) != null) {
                    long countNode = (maxSteps - nodeByPosition.get(new Position(i, j)).getTentativeDistance())/131;
                    //if countNode is even it means that the same ammount of even and odd distances will appear,
                    //so we can just take the result divided by 2
                    if(countNode % 2 == 0) {
                        countNode = countNode/2;
                    } else {
                        //if the first value that will appear is odd, it means we will have one more odd than even
                        if ((nodeByPosition.get(new Position(i, j)).getTentativeDistance() + 131)%2 == 1) {
                            //given that we are interested in the odd values we add plus one to the division by 2
                            countNode = countNode/2 + 1;
                        } else {
                            //otherwise we should not add plus 1
                            countNode = countNode/2;
                        }
                    }
                    countNodes += countNode;
                }
            }
        }

        System.out.println("Valid node count is: " + countNodes);
    }


}