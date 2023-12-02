package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, Integer> COLOR_LIMITS = Map.of("green", 13,
            "blue", 14, "red", 12);

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 2 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        int lineId = 0;
        int idSum = 0;
        while ((line = br.readLine()) != null) {
            lineId++;
            String gameInfo = line.split(": ")[1];
            String[] setsInfo = gameInfo.split("; ");
            Map<String, Integer> ballsPerColorMap = new HashMap<>();
            for (String setInfo : setsInfo) {
                String[] colorsInfo = setInfo.split(", ");
                for(String colorInfo : colorsInfo) {
                    String[] colorInfoData = colorInfo.split(" ");
                    int currentNumberOfBalls = Integer.valueOf(colorInfoData[0]);
                    String currentColor = colorInfoData[1];
                    ballsPerColorMap.compute(currentColor,
                            (color, numberOfBalls) -> numberOfBalls == null ?
                                    currentNumberOfBalls : Math.max(numberOfBalls, currentNumberOfBalls));
                }
            }
            if(isMapPossible(ballsPerColorMap)) {
                idSum += lineId;
            }
        }
        System.out.println("Sum of the IDs of the valid games: " + idSum);
    }

    private static boolean isMapPossible(Map<String, Integer> ballsPerColorMap) {
        return ballsPerColorMap.entrySet().stream().allMatch(entry ->
                entry.getValue() <= COLOR_LIMITS.get(entry.getKey()));
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 2 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        int powerSum = 0;
        while ((line = br.readLine()) != null) {
            String gameInfo = line.split(": ")[1];
            String[] setsInfo = gameInfo.split("; ");
            Map<String, Integer> ballsPerColorMap = new HashMap<>();
            for (String setInfo : setsInfo) {
                String[] colorsInfo = setInfo.split(", ");
                for(String colorInfo : colorsInfo) {
                    String[] colorInfoData = colorInfo.split(" ");
                    int currentNumberOfBalls = Integer.parseInt(colorInfoData[0]);
                    String currentColor = colorInfoData[1];
                    ballsPerColorMap.compute(currentColor,
                            (color, numberOfBalls) -> numberOfBalls == null ?
                                    currentNumberOfBalls : Math.max(numberOfBalls, currentNumberOfBalls));
                }
            }
            powerSum += calculatePower(ballsPerColorMap);
        }
        System.out.println("Sum of the IDs of the valid games: " + powerSum);
    }

    private static int calculatePower(Map<String, Integer> ballsPerColorMap) {
        return ballsPerColorMap.values().stream()
                .reduce(1, (a, b) -> a * b);
    }

}

