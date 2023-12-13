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
        System.out.println("Solving Day 12 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        long numberOfCombinations = 0;
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" ");
            //read final subsets
            List<Subset> finalSubsets = new ArrayList<>();
            Arrays.stream(lineData[1].split(","))
                    .mapToLong(Long::valueOf)
                    .forEach(number -> finalSubsets.add(new Subset(number, true)));
            //create solution to be updated
            Solution solution = new Solution(lineData[0].chars()
                    .mapToObj(e->(char)e).collect(Collectors.toList()));
            //obtain combinations
            numberOfCombinations += solution.solve(finalSubsets);
        }

        System.out.println("The sum of number of combinations is: " + numberOfCombinations);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 12 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        long numberOfCombinations = 0;
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(" ");
            //read final subsets
            List<Subset> finalSubsets = new ArrayList<>();
            Arrays.stream(lineData[1].split(","))
                    .mapToLong(Long::valueOf)
                    .forEach(number -> finalSubsets.add(new Subset(number, true)));
            updateFinalSubsets(finalSubsets);
            //create solution to be updated
            List<Character> characters = lineData[0].chars()
                    .mapToObj(e->(char)e).collect(Collectors.toList());
            updateCharacters(characters);
            Solution solution = new Solution(characters);
            //obtain combinations
            numberOfCombinations += solution.solve(finalSubsets);
        }

        System.out.println("The sum of number of combinations is: " + numberOfCombinations);
    }

    private static void updateCharacters(List<Character> characters) {
        int initialSize = characters.size();
        for(int i = 0; i < 4; i++) {
            characters.add('?');
            for(int j = 0; j < initialSize; j++) {
                characters.add(characters.get(j));
            }
        }
    }

    private static void updateFinalSubsets(List<Subset> finalSubsets) {
        int i = 0;
        while(i < finalSubsets.size()) {
            for(int j = 0; j < 5; j++) {
                finalSubsets.add(i + 1, new Subset(finalSubsets.get(i)));
            }
            i += 5;
        }
    }
}