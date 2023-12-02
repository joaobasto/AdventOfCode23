package org.example;

import java.io.*;
import java.util.Map;
import java.util.Optional;

import static java.lang.Character.isDigit;

public class Main {

    private static final Map<Integer, String> DIGIT_STRING_REPRESENTATIONS = Map.of(
            1, "one",
            2, "two",
            3, "three",
            4, "four",
            5, "five",
            6, "six",
            7, "seven",
            8, "eight",
            9, "nine"
            );

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 1 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        int totalCalibrationValues = 0;
        while ((line = br.readLine()) != null) {
            char[] characters = line.toCharArray();
            int firstDigit = findFirstDigitUntilIndex(characters, characters.length -1).get();
            int lastDigit = findLastDigitFromIndex(characters, 0).get();
            int calibrationValue = 10 * firstDigit + lastDigit;
            totalCalibrationValues += calibrationValue;
        }

        System.out.println("The sum of all of the calibration values is: " + totalCalibrationValues);
    }

    private static Optional<Integer> findFirstDigitUntilIndex(char[] characters, int maxIndex) {
        for (int i = 0; i <= maxIndex; i++) {
            if(isDigit(characters[i])) {
                return Optional.of(Character.getNumericValue(characters[i]));
            }
        }
        return Optional.empty();
    }

    private static Optional<Integer> findLastDigitFromIndex(char[] characters, int minIndex) {
        for (int i = characters.length -1; i >= minIndex; i--) {
            if(isDigit(characters[i])) {
                return Optional.of(Character.getNumericValue(characters[i]));
            }
        }
        return Optional.empty();
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 1 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        int totalCalibrationValues = 0;
        while ((line = br.readLine()) != null) {
            int firstDigit = findFirstNumber(line);
            int lastDigit = findLastNumber(line);
            int calibrationValue = 10 * firstDigit + lastDigit;
            totalCalibrationValues += calibrationValue;
        }

        System.out.println("The sum of all of the calibration values is: " + totalCalibrationValues);
    }

    private static int findFirstNumber(String line) {
        Integer minIndex = null;
        Integer firstStringDigit = null;
        for(Map.Entry<Integer, String> entry : DIGIT_STRING_REPRESENTATIONS.entrySet()) {
            int currentIndex = line.indexOf(entry.getValue());
            if (currentIndex > -1 && (minIndex == null || minIndex > currentIndex)) {
                minIndex = currentIndex;
                firstStringDigit = entry.getKey();
            }
        }
        Optional<Integer> firstDigitOptional = findFirstDigitUntilIndex(line.toCharArray(),
                minIndex == null ? line.length() - 1 : minIndex);
        return firstDigitOptional.orElse(firstStringDigit);
    }


    private static int findLastNumber(String line) {
        Integer maxIndex = null;
        Integer lastStringDigit = null;
        for(Map.Entry<Integer, String> entry : DIGIT_STRING_REPRESENTATIONS.entrySet()) {
            int currentIndex = line.lastIndexOf(entry.getValue());
            if (currentIndex > -1 && (maxIndex == null || maxIndex < currentIndex)) {
                maxIndex = currentIndex;
                lastStringDigit = entry.getKey();
            }
        }
        Optional<Integer> firstDigitOptional = findLastDigitFromIndex(line.toCharArray(),
                maxIndex == null ? 0 : maxIndex);
        return firstDigitOptional.orElse(lastStringDigit);
    }

}
