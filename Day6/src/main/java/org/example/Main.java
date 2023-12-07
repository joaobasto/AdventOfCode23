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
        System.out.println("Solving Day 6 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //read times and distances
        String line = br.readLine();
        String timeData = line.split(":\\s+")[1];
        List<Integer> times = Arrays.stream(timeData.split("\\s+"))
                .map(Integer::valueOf).collect(Collectors.toList());
        line = br.readLine();
        String distanceData = line.split(":\\s+")[1];
        List<Integer> distances = Arrays.stream(distanceData.split("\\s+"))
                .map(Integer::valueOf).collect(Collectors.toList());

        //distance = (totalTime - holdingTime) * holdingTime
        //defining holdingTime as x, we want to solve the equation
        //x² - totalTime * x + recordDistance = 0 to know the range of values our holdingTime can take
        //x = (totalTime +- sqrt(totalTime² - 4 * recordDistance))/2

        List<Integer> rangeSizes = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            double totalTime = (double)times.get(i);
            double recordDistance = (double)distances.get(i);
            double x1 = (totalTime - Math.sqrt(Math.pow(totalTime, 2) - 4 * recordDistance)) / 2;
            double x2 = (totalTime + Math.sqrt(Math.pow(totalTime, 2) - 4 * recordDistance)) / 2;
            //if the values are integer, it means they would match the record but not beat it
            //so in that case, we should add/subtract a decimal number between 0 and 1 to assure correct approximation
            //when calculating the limits
            if (x1 % 1 == 0) {
                x1 = x1 + 0.1;
            }
            if (x2 % 1 == 0) {
                x2 = x2 - 0.1;
            }
            int lowerLimit = (int) Math.ceil(x1);
            int upperLimit = (int) Math.floor(x2);
            int rangeSize = upperLimit - lowerLimit + 1;
            rangeSizes.add(rangeSize);
        }

        int multipliedRangeSizes = rangeSizes.stream().reduce(1, (a, b) -> a * b);

        System.out.println("The multiplied range sizes are: " + multipliedRangeSizes);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 6 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //read times and distances
        String line = br.readLine();
        String timeData = line.split(":\\s+")[1];
        timeData = timeData.replaceAll("\\s+", "");
        long totalTime = Long.parseLong(timeData);
        line = br.readLine();
        String distanceData = line.split(":\\s+")[1];
        distanceData = distanceData.replaceAll("\\s+", "");
        long recordDistance = Long.parseLong(distanceData);

        //distance = (totalTime - holdingTime) * holdingTime
        //defining holdingTime as x, we want to solve the equation
        //x² - totalTime * x + recordDistance = 0 to know the range of values our holdingTime can take
        //x = (totalTime +- sqrt(totalTime² - 4 * recordDistance))/2

        double x1 = (totalTime - Math.sqrt(Math.pow(totalTime, 2) - 4 * recordDistance)) / 2;
        double x2 = (totalTime + Math.sqrt(Math.pow(totalTime, 2) - 4 * recordDistance)) / 2;
        //if the values are integer, it means they would match the record but not beat it
        //so in that case, we should add/subtract a decimal number between 0 and 1 to assure correct approximation
        //when calculating the limits
        if (x1 % 1 == 0) {
            x1 = x1 + 0.1;
        }
        if (x2 % 1 == 0) {
            x2 = x2 - 0.1;
        }
        int lowerLimit = (int) Math.ceil(x1);
        int upperLimit = (int) Math.floor(x2);
        int rangeSize = upperLimit - lowerLimit + 1;

        System.out.println("The range size is: " + rangeSize);
    }


}
