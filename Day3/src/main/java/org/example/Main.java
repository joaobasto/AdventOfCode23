package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 3 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<List<Character>> blueprint = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            List<Character> lineChars = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            blueprint.add(lineChars);
        }
        Map map = new Map(blueprint);
        int totalPartNumbers = map.findTotalPartNumbers();

        System.out.println("The sum of the part numbers is: " + totalPartNumbers);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 3 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<List<Character>> blueprint = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            List<Character> lineChars = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            blueprint.add(lineChars);
        }
        Map map = new Map(blueprint);
        int totalGearRatio = map.findTotalGearRatio();

        System.out.println("The sum of the gear ratios is: " + totalGearRatio);
    }
}