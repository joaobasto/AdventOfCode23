package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.NodeType.*;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 10 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //read blueprint of map into characters
        List<List<Character>> blueprint = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            List<Character> lineChars = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            blueprint.add(lineChars);
        }

        //find start position
        Position startPosition = null;
        for (int i = 0; i < blueprint.size(); i++) {
            for (int j = 0; j < blueprint.get(i).size(); j++) {
                if (blueprint.get(i).get(j).equals('S')) {
                    startPosition = new Position(i, j);
                    break;
                }
            }
        }

        //find initial movement direction
        Direction initialDirection = null;
        //check north
        if(startPosition.getX() > 0) {
            NodeType nodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(startPosition.getX() - 1).get(startPosition.getY())
            );
            if (nodeType.hasDirection(Direction.SOUTH)) {
                initialDirection = Direction.NORTH;
            }
        }
        //check south
        if (initialDirection == null && startPosition.getX() < blueprint.size() - 1) {
            NodeType nodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(startPosition.getX() + 1).get(startPosition.getY())
            );
            if (nodeType.hasDirection(Direction.NORTH)) {
                initialDirection = Direction.SOUTH;
            }
        }
        //check east
        if (initialDirection == null && startPosition.getY() > 0) {
            NodeType nodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(startPosition.getX()).get(startPosition.getY() + 1)
            );
            if (nodeType.hasDirection(Direction.WEST)) {
                initialDirection = Direction.EAST;
            }
        }
        //given that this will be a loop, we don't need to check west because it is certain
        //that we already have a direction to go to

        //we traverse the nodes until we reach the start position again
        int numberOfSteps = 0;
        NodeType currentNodeType = null;
        Direction movementDirection = initialDirection;
        Position currentPosition = startPosition;
        while (true) {
            //update position
            currentPosition = currentPosition.movePosition(
                    movementDirection.getX(),
                    movementDirection.getY());
            //update number of steps
            numberOfSteps++;
            //check current node type
            currentNodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(currentPosition.getX()).get(currentPosition.getY())
            );
            if(currentNodeType == NodeType.START) {
                break;
            }
            //get direction of movement
            movementDirection = currentNodeType.getDirectionTo(Direction.getInverseDirection(movementDirection));
        }

        int maximumDistance = numberOfSteps / 2;
        System.out.println("Maximum distance from start was: " + maximumDistance);

    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 10 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //read blueprint of map into characters
        List<List<Character>> blueprint = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            List<Character> lineChars = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            blueprint.add(lineChars);
        }

        //find start position
        Position startPosition = null;
        for (int i = 0; i < blueprint.size(); i++) {
            for (int j = 0; j < blueprint.get(i).size(); j++) {
                if (blueprint.get(i).get(j).equals('S')) {
                    startPosition = new Position(i, j);
                    break;
                }
            }
        }

        //find initial movement direction
        Direction initialDirection = null;
        List<Direction> startPositionDirections = new ArrayList<>();
        //check north
        if(startPosition.getX() > 0) {
            NodeType nodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(startPosition.getX() - 1).get(startPosition.getY())
            );
            if (nodeType.hasDirection(Direction.SOUTH)) {
                initialDirection = Direction.NORTH;
                startPositionDirections.add(Direction.NORTH);
            }
        }
        //check south
        if (startPosition.getX() < blueprint.size() - 1) {
            NodeType nodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(startPosition.getX() + 1).get(startPosition.getY())
            );
            if (nodeType.hasDirection(Direction.NORTH)) {
                initialDirection = Direction.SOUTH;
                startPositionDirections.add(Direction.SOUTH);
            }
        }
        //check east
        if (startPosition.getY() < blueprint.get(startPosition.getX()).size() - 1) {
            NodeType nodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(startPosition.getX()).get(startPosition.getY() + 1)
            );
            if (nodeType.hasDirection(Direction.WEST)) {
                initialDirection = Direction.EAST;
                startPositionDirections.add(Direction.EAST);
            }
        }
        //given that this will be a loop, we don't need to check west because it is certain
        //that we already have a direction to go to
        // we will just check west to define the start position directions
        //check west
        if (startPosition.getY() > 0) {
            NodeType nodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(startPosition.getX()).get(startPosition.getY() - 1)
            );
            if (nodeType.hasDirection(Direction.EAST)) {
                startPositionDirections.add(Direction.WEST);
            }
        }

        //get start position's node type
        NodeType startNodeType = Arrays.stream(NodeType.values())
                .filter(nodeType -> nodeType.hasDirection(startPositionDirections.get(0)))
                .filter(nodeType -> nodeType.hasDirection(startPositionDirections.get(1)))
                .findFirst().get();

        //we traverse the nodes until we reach the start position again
        Set<Position> loopPositions  = new HashSet<>();
        int numberOfSteps = 0;
        NodeType currentNodeType = null;
        Direction movementDirection = initialDirection;
        Position currentPosition = startPosition;
        while (true) {
            //update position
            currentPosition = currentPosition.movePosition(
                    movementDirection.getX(),
                    movementDirection.getY());
            //update number of steps
            numberOfSteps++;
            //check current node type
            currentNodeType = NodeType.getNodeTypeFromChar(
                    blueprint.get(currentPosition.getX()).get(currentPosition.getY())
            );
            loopPositions.add(currentPosition);
            if(currentNodeType == NodeType.START) {
                blueprint.get(currentPosition.getX()).set(currentPosition.getY(), startNodeType.getCharFromNodeType());
                break;
            }
            //get direction of movement
            movementDirection = currentNodeType.getDirectionTo(Direction.getInverseDirection(movementDirection));
        }

        //now, we have the positions in the loop saved in loopPositions
        //we can do the following to determine if characters are inside the loop
        //for each line, we start by being outside of the loop
        //if we pass by a loop position, depending on the type of node,
        //we may switch being inside/outside the loop
        int tilesInLoop = 0;
        for (int i = 0; i < blueprint.size(); i++) {
            boolean isInsideLoop = false;
            NodeType previousCurve = null;
            for (int j = 0; j < blueprint.get(i).size(); j++) {
                //get node type
                NodeType nodeType = NodeType.getNodeTypeFromChar(
                        blueprint.get(i).get(j)
                );
                if (loopPositions.contains(new Position(i, j))) {
                    //check for loop state inversion
                    if(nodeType.invertsLoopState(previousCurve)) {
                        isInsideLoop = !isInsideLoop;
                    }
                    //update previous curve
                    if(nodeType == NORTH_EAST || nodeType == SOUTH_EAST) {
                        previousCurve = nodeType;
                    }
                } else if(isInsideLoop) {
                    tilesInLoop++;
                }
            }
        }

        System.out.println("Number of tiles inside loop is: " + tilesInLoop);
    }
}