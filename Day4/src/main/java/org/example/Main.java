package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 4 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        long totalPoints = 0;
        while ((line = br.readLine()) != null) {
            String scratchcardData = line.split(":\\s+")[1];
            String[] scratchardSeparatedData = scratchcardData.split("\\s+\\|\\s+");
            Set<Integer> winningNumbers = Arrays.stream(scratchardSeparatedData[0].split("\\s+"))
                    .map(Integer::valueOf).collect(Collectors.toSet());
            Set<Integer> cardNumbers = Arrays.stream(scratchardSeparatedData[1].split("\\s+"))
                    .map(Integer::valueOf).collect(Collectors.toSet());
            long matchingNumbersCount = winningNumbers.stream()
                    .filter(cardNumbers::contains)
                    .count();
            long points = matchingNumbersCount == 0 ? 0 : (long) Math.pow(2, matchingNumbersCount - 1);
            totalPoints += points;
        }
        System.out.println("Total points in scratchcards is: " + totalPoints);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 4 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        Map<Integer, Integer> totalCardsById = new HashMap<>();
        int cardId = 0;
        while ((line = br.readLine()) != null) {
            cardId++;
            totalCardsById.compute(cardId,
                    (k, v) -> v == null ? 1 : v + 1);
            String scratchcardData = line.split(":\\s+")[1];
            String[] scratchardSeparatedData = scratchcardData.split("\\s+\\|\\s+");
            Set<Integer> winningNumbers = Arrays.stream(scratchardSeparatedData[0].split("\\s+"))
                    .map(Integer::valueOf).collect(Collectors.toSet());
            Set<Integer> cardNumbers = Arrays.stream(scratchardSeparatedData[1].split("\\s+"))
                    .map(Integer::valueOf).collect(Collectors.toSet());
            Set<Integer> matchingNumbers = winningNumbers.stream()
                    .filter(cardNumbers::contains)
                    .collect(Collectors.toSet());
            if (matchingNumbers.isEmpty()) {
                continue;
            }
            Integer totalCardsForCurrentId = totalCardsById.get(cardId);
            IntStream.range(cardId + 1, cardId + matchingNumbers.size() + 1)
                    .forEach(number -> totalCardsById.compute(number,
                    (k, v) -> v == null ? totalCardsForCurrentId : v + totalCardsForCurrentId));
        }
        int totalCards = totalCardsById.values().stream().reduce(0, Integer::sum);
        System.out.println("The total number of scratchcards is: " + totalCards);
    }


}