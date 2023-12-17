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
        System.out.println("Solving Day 14 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<List<Position>> blueprint = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            List<Position> positions = line.chars().mapToObj(c -> (char) c)
                    .map(ElementType::fromCharacter)
                    .map(Position::new)
                    .collect(Collectors.toList());
            blueprint.add(positions);
        }

        //move rounded rocks north and calculate load
        long load = 0L;
        for (int i = 0; i < blueprint.size(); i++) {
            for (int j = 0; j < blueprint.get(i).size(); j++) {
                if (blueprint.get(i).get(j).getElementType() == ElementType.ROUNDED_ROCK) {
                    int destinationRow = calculateDestinationRowNorth(blueprint, i, j);
                    blueprint.get(i).get(j).setElementType(ElementType.EMPTY);
                    blueprint.get(destinationRow).get(j).setElementType(ElementType.ROUNDED_ROCK);
                    load += blueprint.size() - destinationRow;
                }
            }
        }

        System.out.println("The total load is: " + load);
    }

    //This solution takes a very long time to run
    //To solve this, I didn't actually run this to the end
    //I run the program long enough to see a pattern on how the load evolves
    //Let's say that this pattern has a period of P
    //Then I took an iteration step i and established that the pattern's first position was the position in step i
    //Then we calculate the difference between step 999999999 and i, and check the remainder of the division by P
    //This will give us the step in P that the final position will be in
    private static void exercise2() throws IOException {
        System.out.println("Solving Day 14 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<List<Position>> blueprint = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            List<Position> positions = line.chars().mapToObj(c -> (char) c)
                    .map(ElementType::fromCharacter)
                    .map(Position::new)
                    .collect(Collectors.toList());
            blueprint.add(positions);
        }

        //get elements and index them by line and column
        List<Element> elements = new ArrayList<>();
        List<List<Element>> elementsByColumn = new ArrayList<>();
        for(int i = 0; i < blueprint.get(0).size(); i++) {
            elementsByColumn.add(new ArrayList<>());
        }
        List<List<Element>> elementsByLine = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < blueprint.size(); i++) {
            elementsByLine.add(new ArrayList<>());
            for (int j = 0; j < blueprint.get(i).size(); j++) {
                if (blueprint.get(i).get(j).getElementType() == ElementType.ROUNDED_ROCK ||
                        blueprint.get(i).get(j).getElementType() == ElementType.CUBED_ROCK) {
                    Element element = new Element(id, blueprint.get(i).get(j).getElementType(), i, j);
                    elements.add(element);
                    elementsByLine.get(elementsByLine.size() -1).add(element);
                    elementsByColumn.get(j).add(element);
                    id++;
                }
            }
        }

        //move rounded rocks north and calculate load
        for(long i = 0; i < 1000000000; i++) {
            moveRocksNorth(elementsByColumn, elementsByLine);
            moveRocksWest(elementsByColumn, elementsByLine);
            moveRocksSouth(elementsByColumn, elementsByLine, blueprint.size());
            moveRocksEast(elementsByColumn, elementsByLine, blueprint.get(0).size());

            //debugging prints used to calculate the solution by the method indicated in the comment
            //before this method
            if(i % 1 == 0) {
                long load = 0L;
                for (Element element : elements) {
                    if (element.getElementType() == ElementType.ROUNDED_ROCK) {
                        load += blueprint.size() - element.getX();
                    }
                }

                System.out.println("(" + i + ", "+ load + ")");
                int offset = (1000000000-1-885)%44;
                System.out.println("Offset: " + offset);
            }
        }

        //calculate load
        long load = 0L;
        for (Element element : elements) {
            if (element.getElementType() == ElementType.ROUNDED_ROCK) {
                load += blueprint.size() - element.getX();
            }
        }

        System.out.println("The total load is: " + load);
    }

    private static void moveRocksNorth(List<List<Element>> elementsByColumn, List<List<Element>> elementsByLine) {
        for (List<Element> columnElements : elementsByColumn) {
            for(int i = 0; i < columnElements.size(); i++) {
                Element element = columnElements.get(i);
                if(element.getElementType() == ElementType.ROUNDED_ROCK) {
                    int newRow = 0;
                    if(i > 0) {
                        newRow = columnElements.get(i -1).getX() + 1;
                    }
                    if(newRow == element.getX()) {
                        continue;
                    }
                    elementsByLine.get(element.getX()).remove(element);
                    element.setX(newRow);
                    int rowIndex = findIndexInRow(elementsByLine.get(element.getX()), element.getY());
                    elementsByLine.get(element.getX()).add(rowIndex, element);
                }
            }
        }
    }

    private static void moveRocksSouth(List<List<Element>> elementsByColumn, List<List<Element>> elementsByLine, int numberOfLines) {
        for (List<Element> columnElements : elementsByColumn) {
            for(int i = columnElements.size() - 1; i >= 0; i--) {
                Element element = columnElements.get(i);
                if(element.getElementType() == ElementType.ROUNDED_ROCK) {
                    int newRow = numberOfLines - 1;
                    if(i < columnElements.size() - 1) {
                        newRow = columnElements.get(i + 1).getX() - 1;
                    }
                    if(newRow == element.getX()) {
                        continue;
                    }
                    elementsByLine.get(element.getX()).remove(element);
                    element.setX(newRow);
                    int rowIndex = findIndexInRow(elementsByLine.get(element.getX()), element.getY());
                    elementsByLine.get(element.getX()).add(rowIndex, element);
                }
            }
        }
    }

    private static void moveRocksWest(List<List<Element>> elementsByColumn, List<List<Element>> elementsByLine) {
        for (List<Element> lineElements : elementsByLine) {
            for(int i = 0; i < lineElements.size(); i++) {
                Element element = lineElements.get(i);
                if(element.getElementType() == ElementType.ROUNDED_ROCK) {
                    int newColumn = 0;
                    if(i > 0) {
                        newColumn = lineElements.get(i -1).getY() + 1;
                    }
                    if(newColumn == element.getY()) {
                        continue;
                    }
                    elementsByColumn.get(element.getY()).remove(element);
                    element.setY(newColumn);
                    int columnIndex = findIndexInColumn(elementsByColumn.get(element.getY()), element.getX());
                    elementsByColumn.get(element.getY()).add(columnIndex, element);
                }
            }
        }
    }

    private static void moveRocksEast(List<List<Element>> elementsByColumn, List<List<Element>> elementsByLine, int numberOfColumns) {
        for (List<Element> lineElements : elementsByLine) {
            for(int i = lineElements.size() - 1; i >= 0; i--) {
                Element element = lineElements.get(i);
                if(element.getElementType() == ElementType.ROUNDED_ROCK) {
                    int newColumn = numberOfColumns - 1;
                    if(i < lineElements.size() - 1) {
                        newColumn = lineElements.get(i + 1).getY() - 1;
                    }
                    if(newColumn == element.getY()) {
                        continue;
                    }
                    elementsByColumn.get(element.getY()).remove(element);
                    element.setY(newColumn);
                    int columnIndex = findIndexInColumn(elementsByColumn.get(element.getY()), element.getX());
                    elementsByColumn.get(element.getY()).add(columnIndex, element);
                }
            }
        }
    }

    private static int findIndexInColumn(List<Element> elements, int x) {
        for(int i = 0; i < elements.size(); i++) {
            if(elements.get(i).getX() > x) {
                return i;
            }
        }
        return elements.size();
    }

    private static int findIndexInRow(List<Element> elements, int y) {
        for(int i = 0; i < elements.size(); i++) {
            if(elements.get(i).getY() > y) {
                return i;
            }
        }
        return elements.size();
    }

    private static int calculateDestinationRowNorth(List<List<Position>> blueprint, int i, int j) {
        for (int k = i - 1; k >= 0; k--) {
            if (blueprint.get(k).get(j).getElementType() != ElementType.EMPTY) {
                return k + 1;
            }
        }
        return 0;
    }
}