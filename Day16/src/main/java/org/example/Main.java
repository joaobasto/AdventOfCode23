package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.Direction.*;
import static org.example.PositionType.getPositionTypeFromCharacter;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 16 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<List<Position>> blueprint = new ArrayList<>();
        String line;
        int rowIndex = 0;
        while ((line = br.readLine()) != null) {
            blueprint.add(new ArrayList<>());
            List<Character> characters = line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            for (int i = 0; i < characters.size(); i++) {
                Position newPosition = new Position(rowIndex, i, getPositionTypeFromCharacter(characters.get(i)));
                blueprint.get(blueprint.size() - 1).add(newPosition);
            }
            rowIndex++;
        }

        List<Beam> movingBeams = new ArrayList<>();
        movingBeams.add(new Beam(true, 0, 0, Direction.RIGHT));

        //this set will be stored to save visited states, i.e, combinations of positions and bean directions
        Set<String> visitedStates = new HashSet<>();
        while(!movingBeams.isEmpty()) {
            for(int i = 0; i < movingBeams.size(); i++) {
                //get beam current position, energize it and store in visitedStates
                Beam currentBeam = movingBeams.get(i);
                Position currentPosition = blueprint.get(currentBeam.getX()).get(currentBeam.getY());
                currentPosition.setEnergized(true);
                visitedStates.add(currentPosition.getX() + ":" + currentPosition.getY() + ":" + currentBeam.getDirection());
                //check in which direction they will move
                List<Direction> directions = currentPosition.getPositionType().determineNewDirection(currentBeam.getDirection());
                currentBeam.setDirection(directions.get(0));
                //update their position
                currentBeam.move();
                //if position crosses map borders, stop the beam
                currentBeam.stopIfBordersCrossed(blueprint.size(), blueprint.get(0).size());
                //stop if it is a visited state to avoid an infinite loop
                if (visitedStates.contains(currentBeam.getX() + ":" + currentBeam.getY() + ":" + currentBeam.getDirection())) {
                    currentBeam.setMoving(false);
                }
                //handle case of new bean created by splitter
                if(directions.size()==2) {
                    //create the new bean
                    Beam newBeam = new Beam(true, currentPosition.getX(), currentPosition.getY(), directions.get(1));
                    movingBeams.add(i + 1, newBeam);
                    //update its position
                    newBeam.move();
                    //check if crosses border and stop if so
                    newBeam.stopIfBordersCrossed(blueprint.size(), blueprint.get(0).size());
                    //stop if it is a visited state to avoid an infinite loop
                    if (visitedStates.contains(newBeam.getX() + ":" + newBeam.getY() + ":" + newBeam.getDirection())) {
                        newBeam.setMoving(false);
                    }
                    //move index to avoid repeating checking this beam
                    i++;
                }
            }
            //check which beans are moving and update list of moving beams
            for(int i = 0; i < movingBeams.size(); i++) {
                if(!movingBeams.get(i).isMoving()) {
                    movingBeams.remove(i);
                    i--;
                }
            }
        }

        long energizedCount = 0L;
        for(List<Position> positions : blueprint) {
            for(Position position: positions) {
                if(position.isEnergized()) {
                    energizedCount++;
                }
            }
        }

        System.out.println("Total number of energized tiles is: " + energizedCount);
    }


    private static void exercise2() throws IOException {
        System.out.println("Solving Day 16 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        List<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        int numberOfLines = lines.size();
        int numberOfColumns = lines.get(0).toCharArray().length;

        //solve iteratively
        long maxEnergizedCount = 0L;
        for(int beamY = 0;  beamY < numberOfColumns; beamY++) {
            long energizedCount = solveForInitialBeamConfiguration(lines, 0, beamY, DOWN);
            if(energizedCount > maxEnergizedCount) {
                maxEnergizedCount = energizedCount;
            }
            energizedCount = solveForInitialBeamConfiguration(lines, numberOfLines - 1, beamY, UP);
            if(energizedCount > maxEnergizedCount) {
                maxEnergizedCount = energizedCount;
            }
        }
        for(int beamX = 0;  beamX < numberOfLines; beamX++) {
            long energizedCount = solveForInitialBeamConfiguration(lines, beamX, 0, RIGHT);
            if(energizedCount > maxEnergizedCount) {
                maxEnergizedCount = energizedCount;
            }
            energizedCount = solveForInitialBeamConfiguration(lines, beamX, numberOfColumns - 1, LEFT);
            if(energizedCount > maxEnergizedCount) {
                maxEnergizedCount = energizedCount;
            }
        }

        System.out.println("Maximum energized tiles: " + maxEnergizedCount);
    }

    private static long solveForInitialBeamConfiguration(List<String> lines, int beamX, int beamY, Direction beamDirection) {
        List<List<Position>> blueprint = new ArrayList<>();
        int rowIndex = 0;
        for(String currentLine : lines) {
            blueprint.add(new ArrayList<>());
            List<Character> characters = currentLine.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            for (int i = 0; i < characters.size(); i++) {
                Position newPosition = new Position(rowIndex, i, getPositionTypeFromCharacter(characters.get(i)));
                blueprint.get(blueprint.size() - 1).add(newPosition);
            }
            rowIndex++;
        }

        List<Beam> movingBeams = new ArrayList<>();
        movingBeams.add(new Beam(true, beamX, beamY, beamDirection));

        Set<String> visitedStates = new HashSet<>();
        while(!movingBeams.isEmpty()) {
            for(int i = 0; i < movingBeams.size(); i++) {
                //get beam current position, energize it and store in visitedStates
                Beam currentBeam = movingBeams.get(i);
                Position currentPosition = blueprint.get(currentBeam.getX()).get(currentBeam.getY());
                currentPosition.setEnergized(true);
                visitedStates.add(currentPosition.getX() + ":" + currentPosition.getY() + ":" + currentBeam.getDirection());
                //check in which direction they will move
                List<Direction> directions = currentPosition.getPositionType().determineNewDirection(currentBeam.getDirection());
                currentBeam.setDirection(directions.get(0));
                //update their position
                currentBeam.move();
                //if position crosses map borders, stop the beam
                currentBeam.stopIfBordersCrossed(blueprint.size(), blueprint.get(0).size());
                //stop if it is a visited state to avoid an infinite loop
                if (visitedStates.contains(currentBeam.getX() + ":" + currentBeam.getY() + ":" + currentBeam.getDirection())) {
                    currentBeam.setMoving(false);
                }
                //handle case of new bean created by splitter
                if(directions.size()==2) {
                    //create the new bean
                    Beam newBeam = new Beam(true, currentPosition.getX(), currentPosition.getY(), directions.get(1));
                    movingBeams.add(i + 1, newBeam);
                    //update its position
                    newBeam.move();
                    //check if crosses border and stop if so
                    newBeam.stopIfBordersCrossed(blueprint.size(), blueprint.get(0).size());
                    //stop if it is a visited state to avoid an infinite loop
                    if (visitedStates.contains(newBeam.getX() + ":" + newBeam.getY() + ":" + newBeam.getDirection())) {
                        newBeam.setMoving(false);
                    }
                    //move index to avoid repeating checking this beam
                    i++;
                }
            }
            //check which beans are moving and update list of moving beams
            for(int i = 0; i < movingBeams.size(); i++) {
                if(!movingBeams.get(i).isMoving()) {
                    movingBeams.remove(i);
                    i--;
                }
            }
        }

        long energizedCount = 0L;
        for(List<Position> positions : blueprint) {
            for(Position position: positions) {
                if(position.isEnergized()) {
                    energizedCount++;
                }
            }
        }

        return energizedCount;
    }
}