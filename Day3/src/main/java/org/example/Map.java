package org.example;

import java.util.*;

public class Map {

    private List<List<Character>> blueprint;
    private int columnPosition = 0;
    private int linePosition = 0;

    public Map(List<List<Character>> blueprint) {
        this.blueprint = blueprint;
    }

    public void setBlueprint(List<List<Character>> blueprint) {
        this.blueprint = blueprint;
    }

    public int findTotalPartNumbers() {
        int totalPartNumbers = 0;
        boolean isCurrentlyInNumber = false;
        int numberStartLinePos = 0;
        int numberEndLinePos;
        for (int i = columnPosition; i < blueprint.size(); i++) {
            for (int j = linePosition; j < blueprint.get(i).size(); j++) {
                if (Character.isDigit(blueprint.get(i).get(j)) &&
                        !isCurrentlyInNumber) {
                    isCurrentlyInNumber = true;
                    numberStartLinePos = j;
                } else if (!Character.isDigit(blueprint.get(i).get(j)) &&
                        isCurrentlyInNumber) {
                    isCurrentlyInNumber = false;
                    numberEndLinePos = j - 1;
                    if (isPartNumber(i, numberStartLinePos, numberEndLinePos)) {
                        totalPartNumbers += getPartNumber(i, numberStartLinePos, numberEndLinePos);
                    }
                }
                if ((j == blueprint.get(i).size() - 1) && isCurrentlyInNumber) {
                    isCurrentlyInNumber = false;
                    numberEndLinePos = j;
                    if (isPartNumber(i, numberStartLinePos, numberEndLinePos)) {
                        totalPartNumbers += getPartNumber(i, numberStartLinePos, numberEndLinePos);
                    }
                }
            }
        }
        return totalPartNumbers;
    }

    private int getPartNumber(int lineNumber, int numberStartLinePos, int numberEndLinePos) {
        int factor = 1;
        int partNumber = 0;
        for(int i = numberEndLinePos; i >= numberStartLinePos; i--) {
            partNumber += factor * Character.getNumericValue(blueprint.get(lineNumber).get(i));
            factor = factor * 10;
        }
        return partNumber;
    }

    private boolean isPartNumber(int lineNumber, int numberStartLinePos, int numberEndLinePos) {
        for(int i = numberStartLinePos; i <= numberEndLinePos; i++) {
            if (lineNumber > 0) {
                if(isSymbol(blueprint.get(lineNumber - 1).get(i))) {
                    return true;
                }
            }
            if(lineNumber < blueprint.size() - 1) {
                if(isSymbol(blueprint.get(lineNumber + 1).get(i))) {
                    return true;
                }
            }
        }

        if (numberStartLinePos > 0) {
            if (isSymbol(blueprint.get(lineNumber).get(numberStartLinePos - 1))) {
                return true;
            }
            if (lineNumber > 0) {
                if(isSymbol(blueprint.get(lineNumber - 1).get(numberStartLinePos - 1))) {
                    return true;
                }
            }
            if(lineNumber < blueprint.size() - 1) {
                if(isSymbol(blueprint.get(lineNumber + 1).get(numberStartLinePos - 1))) {
                    return true;
                }
            }
        }

        if (numberEndLinePos < blueprint.get(lineNumber).size() - 1) {
            if (isSymbol(blueprint.get(lineNumber).get(numberEndLinePos + 1))) {
                return true;
            }
            if (lineNumber > 0) {
                if(isSymbol(blueprint.get(lineNumber - 1).get(numberEndLinePos + 1))) {
                    return true;
                }
            }
            if (lineNumber < blueprint.size() - 1) {
                if (isSymbol(blueprint.get(lineNumber + 1).get(numberEndLinePos + 1))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSymbol(Character character) {
        return !Character.isDigit(character) && !character.equals('.');
    }

    public int findTotalGearRatio() {
        int totalGearRatio = 0;
        for (int i = 0; i < blueprint.size(); i++) {
            for (int j = 0; j < blueprint.get(i).size(); j++) {
                if (isPossibleGear(i, j)) {
                    totalGearRatio += getGearRatio(i, j);
                }
            }
        }
        return totalGearRatio;
    }

    private int getGearRatio(int i, int j) {
        Set<Integer> adjacentNumbers = getAdjacentNumbers(i, j);
        if(adjacentNumbers.size() == 2) {
            return adjacentNumbers.stream().reduce(1, (a, b) -> a*b);
        }
        return 0;
    }

    private boolean isPossibleGear(int lineNumber, int columnNumber) {
        return blueprint.get(lineNumber).get(columnNumber).equals('*');
    }

    private Set<Integer> getAdjacentNumbers(int lineNumber, int columnNumber) {
        Set<Integer> adjacentNumbers = new HashSet<>();
        //check left side
        if (columnNumber > 0) {
            adjacentNumbers.addAll(getNumbersInRange(lineNumber, columnNumber - 1, columnNumber - 1));
        }
        //check right side
        if (columnNumber < blueprint.get(lineNumber).size() - 1) {
            adjacentNumbers.addAll(getNumbersInRange(lineNumber, columnNumber + 1, columnNumber + 1));
        }
        //check above line
        if(lineNumber > 0) {
            adjacentNumbers.addAll(getNumbersInRange(lineNumber - 1,
                    Math.max(columnNumber - 1, 0),
                    Math.min(columnNumber + 1, blueprint.get(lineNumber).size() - 1)));
        }
        //check below line
        if(lineNumber < blueprint.size() - 1) {
            adjacentNumbers.addAll(getNumbersInRange(lineNumber + 1,
                    Math.max(columnNumber - 1, 0),
                    Math.min(columnNumber + 1, blueprint.get(lineNumber).size() - 1)));
        }
        return adjacentNumbers;
    }

    /**
     * Finds all the numbers that have a digit in line lineNumber, between positions lineStartPos and
     * lineEndPos, both inclusively
     * @param lineNumber
     * @param lineStartPos
     * @param lineEndPos
     * @return
     */
    private Set<Integer> getNumbersInRange(int lineNumber, int lineStartPos, int lineEndPos) {
        Set<Integer> numbers = new HashSet<>();
        Integer numberStartLinePos = null;
        Integer numberEndLinePos = null;
        //handle specific case of getting digits from before starting position
        if(Character.isDigit(blueprint.get(lineNumber).get(lineStartPos))) {
            int i = lineStartPos;
            while (Character.isDigit(blueprint.get(lineNumber).get(i))) {
                numberStartLinePos = i;
                if (i==0) {
                    break;
                }
                i--;
            }
            i = lineStartPos;
            while (Character.isDigit(blueprint.get(lineNumber).get(i))) {
                numberEndLinePos = i;
                if (i==blueprint.get(lineNumber).size() - 1) {
                    break;
                }
                i++;
            }
        }

        if(numberStartLinePos != null) {
            numbers.add(getPartNumber(lineNumber, numberStartLinePos, numberEndLinePos));
        }

        int currentLinePos = numberEndLinePos == null ? lineStartPos + 1 : numberEndLinePos + 1;

        numberStartLinePos = null;
        numberEndLinePos = null;
        boolean isCurrentlyInNumber = false;
        for (int i = currentLinePos; i <= lineEndPos; i++) {
            if(Character.isDigit(blueprint.get(lineNumber).get(i)) && !isCurrentlyInNumber) {
                isCurrentlyInNumber = true;
                numberStartLinePos = i;
            }
            if(!Character.isDigit(blueprint.get(lineNumber).get(i)) && isCurrentlyInNumber) {
                isCurrentlyInNumber = false;
                numberEndLinePos = i - 1;
                numbers.add(getPartNumber(lineNumber, numberStartLinePos, numberEndLinePos));
                numberStartLinePos = null;
                numberEndLinePos = null;
            }
        }

        if(numberStartLinePos == null) {
            return numbers;
        }

        //handle specific case of getting digits from after ending position
        int i = numberStartLinePos;
        while (Character.isDigit(blueprint.get(lineNumber).get(i))) {
            numberEndLinePos = i;
            if (i==blueprint.get(lineNumber).size() - 1) {
                break;
            }
            i++;
        }
        numbers.add(getPartNumber(lineNumber, numberStartLinePos, numberEndLinePos));
        return numbers;
    }
}
