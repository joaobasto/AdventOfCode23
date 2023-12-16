package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 13 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<List<Character>> blueprint = new ArrayList<>();
        String line;
        long result = 0L;
        while ((line = br.readLine()) != null) {
            if(line.isEmpty()) {
                result += calculateVerticalReflection(blueprint);
                result += calculateHorizontalReflection(blueprint);
                blueprint = new ArrayList<>();
            } else {
                List<Character> lineChars = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                blueprint.add(lineChars);
            }
        }
        result += calculateVerticalReflection(blueprint);
        result += calculateHorizontalReflection(blueprint);

        System.out.println("The result was: " + result);
    }

    /**
     * Returns the number of columns to the left of the reflection
     * @param blueprint
     * @return
     */
    private static long calculateVerticalReflection(List<List<Character>> blueprint) {
        for (int i = 0; i < blueprint.get(0).size() - 1; i++) {
            boolean isReflection = true;
            int numberOfColumnsToCheck = Math.min(i+1, blueprint.get(0).size() - i - 1);
            for (int j = 0; j < numberOfColumnsToCheck; j++) {
                for (int k = 0; k < blueprint.size(); k++) {
                    if (!blueprint.get(k).get(i - j).equals(blueprint.get(k).get(i + 1 + j))) {
                        isReflection = false;
                        break;
                    }
                }
                if (!isReflection) {
                    break;
                }
            }
            if(isReflection) {
                return i + 1;
            }
        }
        return 0L;
    }

    /**
     * Returns the number of rows above the reflection multiplied by 100
     * @param blueprint
     * @return
     */
    private static long calculateHorizontalReflection(List<List<Character>> blueprint) {
        for (int i = 0; i < blueprint.size() - 1; i++) {
            boolean isReflection = true;
            int numberOfLinesToCheck = Math.min(i + 1, blueprint.size() - i - 1);
            for (int j = 0; j < numberOfLinesToCheck; j++) {
                for (int k = 0; k < blueprint.get(0).size(); k++) {
                    if(!blueprint.get(i - j).get(k).equals(blueprint.get(i +1 + j).get(k))) {
                        isReflection = false;
                        break;
                    }
                }
                if (!isReflection) {
                    break;
                }
            }
            if(isReflection) {
                return 100L * (i + 1);
            }
        }
        return 0L;
    }


    private static void exercise2() throws IOException {
        System.out.println("Solving Day 13 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<List<Character>> blueprint = new ArrayList<>();
        String line;
        long result = 0L;
        while ((line = br.readLine()) != null) {
            if(line.isEmpty()) {
                result += calculateVerticalReflectionSmudgeValue(blueprint);
                result += calculateHorizontalReflectionSmudgeValue(blueprint);
                blueprint = new ArrayList<>();
            } else {
                List<Character> lineChars = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                blueprint.add(lineChars);
            }
        }
        result += calculateVerticalReflectionSmudgeValue(blueprint);
        result += calculateHorizontalReflectionSmudgeValue(blueprint);

        System.out.println("The result was: " + result);
    }

    private static long calculateHorizontalReflectionSmudgeValue(List<List<Character>> blueprint) {
        for (int i = 0; i < blueprint.size() - 1; i++) {
            int numberOfInconsistencies = 0;
            int numberOfLinesToCheck = Math.min(i + 1, blueprint.size() - i - 1);
            for (int j = 0; j < numberOfLinesToCheck; j++) {
                for (int k = 0; k < blueprint.get(0).size(); k++) {
                    if(!blueprint.get(i - j).get(k).equals(blueprint.get(i +1 + j).get(k))) {
                        numberOfInconsistencies++;
                        if(numberOfInconsistencies > 1) {
                            break;
                        }
                    }
                }
                if(numberOfInconsistencies > 1) {
                    break;
                }
            }
            if(numberOfInconsistencies == 1) {
                return 100L * (i + 1);
            }
        }
        return 0L;
    }

    private static long calculateVerticalReflectionSmudgeValue(List<List<Character>> blueprint) {
        for (int i = 0; i < blueprint.get(0).size() - 1; i++) {
            int numberOfInconsistencies = 0;
            int numberOfColumnsToCheck = Math.min(i+1, blueprint.get(0).size() - i - 1);
            for (int j = 0; j < numberOfColumnsToCheck; j++) {
                for (int k = 0; k < blueprint.size(); k++) {
                    if (!blueprint.get(k).get(i - j).equals(blueprint.get(k).get(i + 1 + j))) {
                        numberOfInconsistencies++;
                        if(numberOfInconsistencies > 1) {
                            break;
                        }
                    }
                }
                if(numberOfInconsistencies > 1) {
                    break;
                }
            }
            if(numberOfInconsistencies == 1) {
                return i + 1;
            }
        }
        return 0L;
    }
}