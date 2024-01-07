package org.example;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 22 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //reading brick information
        List<Brick> bricks = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] positions = line.split(",|~");
            bricks.add(new Brick(Long.parseLong(positions[0]), Long.parseLong(positions[1]), Long.parseLong(positions[2]),
                    Long.parseLong(positions[3]), Long.parseLong(positions[4]), Long.parseLong(positions[5])));
        }

        //sorting the bricks in a priority queue by their min-z value
        PriorityQueue<Brick> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(brick -> (int) brick.posZMin));
        priorityQueue.addAll(bricks);

        //create a set to hold each XY position as well as its data
        Map<Position, PositionData> positions = new HashMap<>();

        //let the bricks fall one by one and update the both the position map with the current heigh and current highest brick
        //as well as the supporting bricks for the falling brick
        while (!priorityQueue.isEmpty()) {
            Brick nextBrick = priorityQueue.poll();
            //get max height along all positions and which bricks have it
            long maxHeight = 0;
            List<Brick> maxHeightBricks = new ArrayList<>();
            for (long i = nextBrick.posXMin; i <= nextBrick.posXMax; i++) {
                for (long j = nextBrick.posYMin; j <= nextBrick.posYMax; j++) {
                    PositionData positionData = positions
                            .compute(new Position(i, j), (k, v) -> v == null ? new PositionData() : v);
                    if (positionData.currentHeight > maxHeight) {
                        maxHeight = positionData.currentHeight;
                        maxHeightBricks.clear();
                        maxHeightBricks.add(positionData.currentHeighestBrick);
                    } else if (positionData.currentHeight == maxHeight) {
                        if (positionData.currentHeighestBrick != null) {
                            maxHeightBricks.add(positionData.currentHeighestBrick);
                        }
                    }
                }
            }
            //update this brick's supporting bricks
            nextBrick.supportingBricks.addAll(maxHeightBricks);
            //update the height and current highest brick of the positions this brick will fall into
            for (long i = nextBrick.posXMin; i <= nextBrick.posXMax; i++) {
                for (long j = nextBrick.posYMin; j <= nextBrick.posYMax; j++) {
                    PositionData positionData = positions.get(new Position(i, j));
                    positionData.currentHeight = maxHeight + (nextBrick.posZMax - nextBrick.posZMin + 1);
                    positionData.currentHeighestBrick = nextBrick;
                }
            }
        }

        //create a set initially with all bricks
        Set<Brick> safeToDisintegrateBricks = new HashSet<>(bricks);
        //remove from the set all bricks that are the single supporting brick of another brick
        for(Brick brick : bricks) {
            if (brick.supportingBricks.size() == 1) {
                safeToDisintegrateBricks.remove(brick.supportingBricks.get(0));
            }
        }

        System.out.println("Number of bricks that can be safely disintegrated: " + safeToDisintegrateBricks.size());

    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 22 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
        }
    }
}