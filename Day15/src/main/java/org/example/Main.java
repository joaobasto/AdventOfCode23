package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 15 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        long hashSum = 0L;
        while ((line = br.readLine()) != null) {
            String[] strings = line.split(",");
            for(String step : strings) {
                char[] chars = step.toCharArray();
                hashSum += hash(chars);
            }
        }

        System.out.println("Hash sum is: " + hashSum);
    }

    private static long hash(char[] chars) {
        long value = 0L;
        for(char character : chars) {
            value += (int) character;
            value = value * 17;
            value = value % 256;
        }
        return value;
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 15 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        Map<Long, Box> boxById = new HashMap<>();
        String line;
        long hashSum = 0L;
        while ((line = br.readLine()) != null) {
            String[] strings = line.split(",");
            for(String step : strings) {
                String label = step.split("-|=")[0];
                String remainingStep = step.substring(label.length());
                String operation = remainingStep.substring(0, 1);
                String focalStrength = null;
                if(operation.equals("=")) {
                    focalStrength = remainingStep.substring(1);
                }
                long boxId = hash(label.toCharArray());
                boxById.computeIfAbsent(boxId, Box::new);
                if(operation.equals("-")) {
                    boxById.get(boxId).getLensByLabel().remove(label);
                } else if(operation.equals("=")) {
                    if(boxById.get(boxId).getLensByLabel().containsKey(label)) {
                        boxById.get(boxId).getLensByLabel().get(label).setFocalStrength(Integer.parseInt(focalStrength));
                    }
                    else {
                        boxById.get(boxId).getLensByLabel().put(label, new Lens(label, Integer.parseInt(focalStrength)));
                    }
                }
            }
        }

        long totalFocusingPower = 0L;
        for(Box box : boxById.values()) {
            int lensPosition = 1;
            for(Lens lens : box.getLensByLabel().values()) {
                totalFocusingPower += (box.getId() + 1) * lens.getFocalStrength() * lensPosition;
                lensPosition++;
            }
        }

        System.out.println("The total focusing power is: " + totalFocusingPower);
    }
}