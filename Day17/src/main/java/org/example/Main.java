package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.Direction.*;

public class Main {

    private static Random randomGenerator = new Random();

    public static void main(String[] args) throws IOException {
        //exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 17 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //define reward map
        Map<Position, Integer> rewardMap = new HashMap<>();
        int rowIndex = 0;
        String line;
        List<Integer> values = null;
        int maximumRewardValue = 0;
        while ((line = br.readLine()) != null) {
            values = line.chars().mapToObj(c -> (char) c)
                    .map(character -> Integer.valueOf(String.valueOf(character)))
                    .collect(Collectors.toList());
            for(int columnIndex = 0; columnIndex < values.size(); columnIndex++) {
                Position position = new Position(rowIndex, columnIndex);
                rewardMap.put(position, -values.get(columnIndex));
                if(values.get(columnIndex) > maximumRewardValue) {
                    maximumRewardValue = values.get(columnIndex);
                }
            }
            rowIndex++;
        }
        int numberOfLines = rowIndex;
        int numberOfColumns = values.size();
        final int maxReward = maximumRewardValue;
        rewardMap.computeIfPresent(new Position(numberOfLines - 1, numberOfColumns -1),
                (k, v) -> v +
                (numberOfLines + numberOfColumns) * Math.abs(maxReward));


        //define value map and policy map
        Map<State, Integer> valueMap = new HashMap<>();
        Map<State, Direction> policyMap = new HashMap<>();
        for (int i = 0; i < numberOfLines; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                for (int consecutiveSteps = 1; consecutiveSteps <= 3; consecutiveSteps++) {
                    for (Direction direction : getAvailableDirectionsFrom(i, j, numberOfLines, numberOfColumns)) {
                        State state = new State(new Position(i, j), direction, consecutiveSteps);
                        valueMap.put(state, randomGenerator.nextInt(4));
                        List<Direction> directions = getAvailableDirectionsToWIthSteps(i, j, numberOfLines, numberOfColumns,
                                consecutiveSteps, direction);
                        if(i == numberOfLines - 1 && j == numberOfColumns - 1) {
                            valueMap.put(state, 0);
                            continue;
                        }
                        policyMap.put(state, directions.get(0));
                    }
                }
            }
        }
        //add edge case of initial position, we will assume the direction is right
        //and set the initial direction to follow in the policy as right also
        valueMap.put(new State(new Position(0, 0), RIGHT, 0), randomGenerator.nextInt(4));
        policyMap.put(new State(new Position(0, 0), RIGHT, 0), RIGHT);

        while (true) {
            //policy evaluation
            int delta = 1000;
            while (delta > 0.005) {
                delta = 0;
                for (State state : valueMap.keySet()) {
                    int oldValue = valueMap.get(state);
                    Direction direction = policyMap.get(state);
                    //if direction is null it's because it is a terminal state, so we continue
                    if (direction == null) {
                        continue;
                    }
                    int consecutiveSteps;
                    if(direction == state.getDirection()) {
                        consecutiveSteps = state.getConsecutiveSteps() + 1;
                    } else {
                        consecutiveSteps = 1;
                    }
                    Position newPosition = state.getPosition().getPositionAfterMove(direction);
                    State newState = new State(newPosition, direction, consecutiveSteps);
                    int totalReward = rewardMap.get(newPosition) + valueMap.get(newState);
                    valueMap.put(state, totalReward);
                    if (Math.abs(totalReward - oldValue) > delta) {
                        delta = Math.abs(totalReward - oldValue);
                    }
                }
            }
            System.out.println("Finished a evaluation stage");
            //policy improvement
            boolean policyStable = true;
            for (State state : policyMap.keySet()) {
                Direction oldDirection = policyMap.get(state);
                List<Direction> availableDirections = getAvailableDirectionsToWIthSteps(state.getPosition().getX(), state.getPosition().getY(),
                        numberOfLines, numberOfColumns, state.getConsecutiveSteps(), state.getDirection());
                boolean isFirst = true;
                Direction bestDirection = null;
                int bestReward = 0;
                for (Direction direction : availableDirections) {
                    Position newPosition = state.getPosition().getPositionAfterMove(direction);
                    int consecutiveSteps;
                    if(direction == state.getDirection()) {
                        consecutiveSteps = state.getConsecutiveSteps() + 1;
                    } else {
                        consecutiveSteps = 1;
                    }
                    State newState = new State(newPosition, direction, consecutiveSteps);
                    int totalReward = rewardMap.get(newPosition) + valueMap.get(newState);
                    if (isFirst) {
                        bestDirection = direction;
                        bestReward = totalReward;
                        isFirst = false;
                    } else {
                        if (totalReward > bestReward) {
                            bestReward = totalReward;
                            bestDirection = direction;
                        }
                    }
                }
                if(bestDirection != oldDirection) {
                    policyStable = false;
                    policyMap.put(state, bestDirection);
                }
            }
            System.out.println("Finished a improvement stage");
            if (policyStable) {
                break;
            }
        }

        int trueRewardValue = valueMap.get(new State(new Position(0, 0), RIGHT, 0)) - (numberOfLines + numberOfColumns) * Math.abs(maximumRewardValue);
        System.out.println("The total heat loss was: " + (-trueRewardValue));
    }

    public static List<Direction> getAvailableDirectionsFrom(int i, int j, int numberOfLines, int numberOfColumns) {
        List<Direction> directions = Arrays.stream(values()).collect(Collectors.toList());
        if (i == 0) {
            directions.remove(DOWN);
        }
        if(j == 0) {
            directions.remove(RIGHT);
        }
        if(i == numberOfLines - 1) {
            directions.remove(UP);
        }
        if(j == numberOfColumns - 1) {
            directions.remove(LEFT);
        }
        return directions;
    }

    public static List<Direction> getAvailableDirectionsToWIthSteps(int i, int j, int numberOfLines, int numberOfColumns,
                                                                    int consecutiveSteps, Direction currentDirection) {
        List<Direction> directions = Arrays.stream(values()).collect(Collectors.toList());
        //map boundaries
        if (i == 0) {
            directions.remove(UP);
        }
        if(j == 0) {
            directions.remove(LEFT);
        }
        if(i == numberOfLines - 1) {
            directions.remove(DOWN);
        }
        if(j == numberOfColumns - 1) {
            directions.remove(RIGHT);
        }
        //maximum consecutive steps
        if(consecutiveSteps > 2) {
            directions.remove(currentDirection);
        }
        //no turning back, only 90 degrees curves or going ahead
        if(currentDirection == UP) {
            directions.remove(DOWN);
        }
        if(currentDirection == DOWN) {
            directions.remove(UP);
        }
        if(currentDirection == LEFT) {
            directions.remove(RIGHT);
        }
        if(currentDirection == RIGHT) {
            directions.remove(LEFT);
        }
        return directions;
    }

    public static List<Direction> getAvailableDirectionsToWIthSteps2(int i, int j, int numberOfLines, int numberOfColumns,
                                                                    int consecutiveSteps, Direction currentDirection) {
        List<Direction> directions = Arrays.stream(values()).collect(Collectors.toList());
        //minimum consecutive steps
        if(consecutiveSteps < 4 && consecutiveSteps > 0) {
            directions = new ArrayList<>();
            directions.add(currentDirection);
        }
        //map boundaries
        if (i == 0) {
            directions.remove(UP);
        }
        if(j == 0) {
            directions.remove(LEFT);
        }
        if(i == numberOfLines - 1) {
            directions.remove(DOWN);
        }
        if(j == numberOfColumns - 1) {
            directions.remove(RIGHT);
        }
        //no turning back, only 90 degrees curves or going ahead
        if(currentDirection == UP) {
            directions.remove(DOWN);
        }
        if(currentDirection == DOWN) {
            directions.remove(UP);
        }
        if(currentDirection == LEFT) {
            directions.remove(RIGHT);
        }
        if(currentDirection == RIGHT) {
            directions.remove(LEFT);
        }
        return directions;
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 17 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //define reward map
        Map<Position, Integer> rewardMap = new HashMap<>();
        int rowIndex = 0;
        String line;
        List<Integer> values = null;
        int maximumRewardValue = 0;
        while ((line = br.readLine()) != null) {
            values = line.chars().mapToObj(c -> (char) c)
                    .map(character -> Integer.valueOf(String.valueOf(character)))
                    .collect(Collectors.toList());
            for(int columnIndex = 0; columnIndex < values.size(); columnIndex++) {
                Position position = new Position(rowIndex, columnIndex);
                rewardMap.put(position, -values.get(columnIndex));
                if(values.get(columnIndex) > maximumRewardValue) {
                    maximumRewardValue = values.get(columnIndex);
                }
            }
            rowIndex++;
        }
        int numberOfLines = rowIndex;
        int numberOfColumns = values.size();
        final int maxReward = maximumRewardValue;
        rewardMap.computeIfPresent(new Position(numberOfLines - 1, numberOfColumns -1),
                (k, v) -> v +
                        (numberOfLines + numberOfColumns) * Math.abs(maxReward));


        //define value map and policy map
        Map<State, Integer> valueMap = new HashMap<>();
        Map<State, Direction> policyMap = new HashMap<>();
        for (int i = 0; i < numberOfLines; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                for (int consecutiveSteps = 1; consecutiveSteps <= 4; consecutiveSteps++) {
                    for (Direction direction : getAvailableDirectionsFrom(i, j, numberOfLines, numberOfColumns)) {
                        State state = new State(new Position(i, j), direction, consecutiveSteps);
                        valueMap.put(state, randomGenerator.nextInt(4));
                        List<Direction> directions = getAvailableDirectionsToWIthSteps2(i, j, numberOfLines, numberOfColumns,
                                consecutiveSteps, direction);
                        if((i == numberOfLines - 1 && j == numberOfColumns - 1) || directions.isEmpty()) {
                            valueMap.put(state, 0);
                            continue;
                        }
                        policyMap.put(state, directions.get(0));
                    }
                }
            }
        }
        //add edge case of initial position, we will assume the direction is right
        //and set the initial direction to follow in the policy as right also
        valueMap.put(new State(new Position(0, 0), RIGHT, 0), randomGenerator.nextInt(4));
        policyMap.put(new State(new Position(0, 0), RIGHT, 0), RIGHT);

        while (true) {
            //policy evaluation
            int delta = 1000;
            while (delta > 0.005) {
                delta = 0;
                for (State state : valueMap.keySet()) {
                    int oldValue = valueMap.get(state);
                    Direction direction = policyMap.get(state);
                    //if direction is null it's because it is a terminal state, so we continue
                    if (direction == null) {
                        continue;
                    }
                    int consecutiveSteps;
                    if(direction == state.getDirection() && state.getConsecutiveSteps() < 4) {
                        consecutiveSteps = state.getConsecutiveSteps() + 1;
                    } else if (direction == state.getDirection() && state.getConsecutiveSteps() == 4) {
                        consecutiveSteps = 4;
                    } else {
                        consecutiveSteps = 1;
                    }
                    Position newPosition = state.getPosition().getPositionAfterMove(direction);
                    State newState = new State(newPosition, direction, consecutiveSteps);
                    int totalReward = rewardMap.get(newPosition) + valueMap.get(newState);
                    valueMap.put(state, totalReward);
                    if (Math.abs(totalReward - oldValue) > delta) {
                        delta = Math.abs(totalReward - oldValue);
                    }
                }
            }
            System.out.println("Finished a evaluation stage");
            //policy improvement
            boolean policyStable = true;
            for (State state : policyMap.keySet()) {
                Direction oldDirection = policyMap.get(state);
                List<Direction> availableDirections = getAvailableDirectionsToWIthSteps2(state.getPosition().getX(), state.getPosition().getY(),
                        numberOfLines, numberOfColumns, state.getConsecutiveSteps(), state.getDirection());
                boolean isFirst = true;
                Direction bestDirection = null;
                int bestReward = 0;
                for (Direction direction : availableDirections) {
                    Position newPosition = state.getPosition().getPositionAfterMove(direction);
                    int consecutiveSteps;
                    if(direction == state.getDirection() && state.getConsecutiveSteps() < 4) {
                        consecutiveSteps = state.getConsecutiveSteps() + 1;
                    } else if (direction == state.getDirection() && state.getConsecutiveSteps() == 4) {
                        consecutiveSteps = 4;
                    } else {
                        consecutiveSteps = 1;
                    }
                    State newState = new State(newPosition, direction, consecutiveSteps);
                    int totalReward = rewardMap.get(newPosition) + valueMap.get(newState);
                    if (isFirst) {
                        bestDirection = direction;
                        bestReward = totalReward;
                        isFirst = false;
                    } else {
                        if (totalReward > bestReward) {
                            bestReward = totalReward;
                            bestDirection = direction;
                        }
                    }
                }
                if(bestDirection != oldDirection) {
                    policyStable = false;
                    policyMap.put(state, bestDirection);
                }
            }
            System.out.println("Finished a improvement stage");
            if (policyStable) {
                break;
            }
        }

        int trueRewardValue = valueMap.get(new State(new Position(0, 0), RIGHT, 0)) - (numberOfLines + numberOfColumns) * Math.abs(maximumRewardValue);
        System.out.println("The total heat loss was: " + (-trueRewardValue));
    }
}