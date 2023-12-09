package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 9 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        Long sumExtrapolatedValues = 0L;
        while ((line = br.readLine()) != null) {
            List<Long> sequence = Arrays.stream(line.split(" "))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            List<List<Long>> subsequences= generateSubsequences(sequence);
            addNextElement(subsequences);
            Long extrapolatedValue = subsequences.get(0).get(subsequences.get(0).size() - 1);
            sumExtrapolatedValues += extrapolatedValue;
        }
        System.out.println("The sum of the extrapolated values is: " + sumExtrapolatedValues);
    }

    private static List<List<Long>> generateSubsequences(List<Long> initialSequence) {
        List<Long> currentSequence = initialSequence;
        List<List<Long>> subsequences = new ArrayList<>();
        subsequences.add(initialSequence);
        while (!isZeros(currentSequence)) {
            List<Long> newSequence = new ArrayList<>();
            for(int i = 1; i < currentSequence.size(); i++) {
                newSequence.add(currentSequence.get(i) - currentSequence.get(i-1));
            }
            subsequences.add(newSequence);
            currentSequence = newSequence;
        }
        return subsequences;
    }

    private static void addNextElement(List<List<Long>> subsequences) {
        subsequences.get(subsequences.size() - 1).add(0L);
        for (int i = subsequences.size() - 2; i >= 0; i--) {
            subsequences.get(i).add(
                    subsequences.get(i).get(subsequences.get(i).size() - 1) +
                    subsequences.get(i + 1).get(subsequences.get(i + 1).size() - 1));
        }
    }

    private static void addBeforeElement(List<List<Long>> subsequences) {
        subsequences.get(subsequences.size() - 1).add(0, 0L);
        for (int i = subsequences.size() - 2; i >= 0; i--) {
            subsequences.get(i).add(0,
                    subsequences.get(i).get(0) -
                            subsequences.get(i + 1).get(0));
        }
    }

    private static boolean isZeros(List<Long> currentSequence) {
        return currentSequence.stream().allMatch(value -> value.equals(0L));
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 9 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        Long sumExtrapolatedValues = 0L;
        while ((line = br.readLine()) != null) {
            List<Long> sequence = Arrays.stream(line.split(" "))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            List<List<Long>> subsequences= generateSubsequences(sequence);
            addBeforeElement(subsequences);
            Long extrapolatedValue = subsequences.get(0).get(0);
            sumExtrapolatedValues += extrapolatedValue;
        }
        System.out.println("The sum of the extrapolated values is: " + sumExtrapolatedValues);
    }
}