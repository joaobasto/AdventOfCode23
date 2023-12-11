package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {


    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 11 Challenge 1: ");
        solver(2L);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 11 Challenge 2: ");
        solver(1000000L);
    }

    private static void solver(long expansionFactor) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        //read blueprint of map into characters
        List<List<Character>> blueprint = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            List<Character> lineChars = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            blueprint.add(lineChars);
        }

        //determine expanded rows
        Set<Integer> expandedRowsIndexes = new HashSet<>();
        int rowIter = 0;
        while (rowIter < blueprint.size() - 1) {
            //check if row has no galaxies
            boolean hasNoGalaxies = blueprint.get(rowIter).stream()
                    .noneMatch(character -> character.equals('#'));
            if (hasNoGalaxies) {
                expandedRowsIndexes.add(rowIter);
            }
            rowIter++;
        }
        //determine expanded columns
        Set<Integer> expandedColumnsIndexes = new HashSet<>();
        int columnIter = 0;
        while (columnIter < blueprint.get(0).size() - 1) {
            //check if column has no galaxies
            int tempColumnIter = columnIter;
            boolean hasNoGalaxies = IntStream.range(0, blueprint.size())
                    .mapToObj(rowIndex -> blueprint.get(rowIndex).get(tempColumnIter))
                    .noneMatch(character -> character.equals('#'));
            if (hasNoGalaxies) {
                expandedColumnsIndexes.add(columnIter);
            }
            columnIter++;
        }

        //generate galaxy information
        List<Galaxy> galaxies = new ArrayList<>();
        int galaxyId = 0;
        for(int i = 0; i < blueprint.size(); i++) {
            for(int j = 0; j < blueprint.get(i).size(); j++) {
                if (blueprint.get(i).get(j).equals('#')) {
                    galaxies.add(new Galaxy(galaxyId, i, j));
                    galaxyId++;
                }
            }
        }

        //get total distance
        long totalDistance = 0;
        for(Galaxy galaxy : galaxies) {
            for(Galaxy otherGalaxy : galaxies) {
                if(galaxy.getId() < otherGalaxy.getId()) {
                    totalDistance += getDistanceBetweenGalaxies(galaxy, otherGalaxy,
                            expandedRowsIndexes, expandedColumnsIndexes, expansionFactor);
                }
            }
        }

        System.out.println("The total distance between pairs of galaxies is: " + totalDistance);
    }

    public static long getDistanceBetweenGalaxies(Galaxy galaxy1, Galaxy galaxy2,
                                          Set<Integer> expandedRowsIndexes, Set<Integer> expandedColumnsIndexes,
                                                 Long expansionFactor) {
        long expandedRowsBetweenGalaxies = expandedRowsIndexes.stream().filter(rowIndex -> isInRange(rowIndex, galaxy1.getX(), galaxy2.getX())).count();
        long expandedColumnsBetweenGalaxies = expandedColumnsIndexes.stream().filter(rowIndex -> isInRange(rowIndex, galaxy1.getY(), galaxy2.getY())).count();

        return Math.abs(galaxy1.getX() - galaxy2.getX()) - expandedRowsBetweenGalaxies
                + expandedRowsBetweenGalaxies * expansionFactor
                + Math.abs(galaxy1.getY() - galaxy2.getY()) - expandedColumnsBetweenGalaxies
                + expandedColumnsBetweenGalaxies * expansionFactor;
    }

    private static boolean isInRange(long number, long x1, long x2) {
        if(x1 < x2) {
            if(number > x1 && number < x2) {
                return true;
            }
        }
        if(x1 > x2) {
            if(number > x2 && number < x1) {
                return true;
            }
        }
        return false;
    }
}