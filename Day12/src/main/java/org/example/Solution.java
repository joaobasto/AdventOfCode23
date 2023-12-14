package org.example;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

    /**
     * Calculates number of feasible solutions that can be obtained.
     * @return
     */
    public static long solve(List<Character> characters,
                      int charactersIndex,
                      List<Subset> subsets,
                      List<Subset> finalSubsets,
                      Map<String, Long> solutionMap) {
        //read and update data until next decision point
        while(!characters.get(charactersIndex).equals('?')) {
            //if it is a point, we get the last subset in our subsets (if such exists and is unbounded)
            //and we bound it
            if(characters.get(charactersIndex).equals('.')) {
                if(!subsets.isEmpty() && !subsets.get(subsets.size() - 1).isBounded()) {
                    subsets.get(subsets.size() - 1).setBounded(true);
                }
            }
            //if it is a #, we check if our last subset in our subsets is unbounded. If so,
            //we had one more to it. If the last subset is bounded or there are no subsets,
            //we create a new subset and add one to it
            if(characters.get(charactersIndex).equals('#')) {
                if(subsets.isEmpty() || subsets.get(subsets.size() - 1).isBounded()) {
                    subsets.add(new Subset(1, false));
                } else if(!subsets.isEmpty() && !subsets.get(subsets.size() - 1).isBounded()) {
                    subsets.get(subsets.size() - 1).increment();
                }
            }
            charactersIndex++;
            if(charactersIndex == characters.size()) {
                break;
            }
        }

        //if we reached the end of the characters we bound our last set
        if(charactersIndex == characters.size() && !subsets.isEmpty()) {
            subsets.get(subsets.size() - 1).setBounded(true);
        }

        //check if we already have a solution for this point
        if (solutionMap.get(convertToMapKey(charactersIndex, subsets)) != null) {
            return solutionMap.get(convertToMapKey(charactersIndex, subsets));
        }

        //check if this is a terminal state:
        //unfeasible state:
        //we have more points than what is possible in total
        long currentTotalPoints = subsets.stream().mapToLong(Subset::getQuantity).sum();
        long finalTotalPoints = finalSubsets.stream().mapToLong(Subset::getQuantity).sum();
        if(currentTotalPoints > finalTotalPoints) {
            solutionMap.put(convertToMapKey(charactersIndex, subsets), 0L);
            return 0L;
        }
        //we have less maximum possible points than the total points
        long maximumTotalPoints = currentTotalPoints +
                characters.subList(charactersIndex, characters.size()).stream()
                        .filter(character -> character.equals('?') || character.equals('#'))
                        .count();
        if(maximumTotalPoints < finalTotalPoints) {
            solutionMap.put(convertToMapKey(charactersIndex, subsets), 0L);
            return 0L;
        }
        //or our current sets will never be able to match the final sets
        if (!isMatchPossible(subsets, finalSubsets)) {
            solutionMap.put(convertToMapKey(charactersIndex, subsets), 0L);
            return 0L;
        }
        //or because it is a feasible solution (we reached the end of the characters
        //and we have the sets of points equal to the desired sets of points)
        if (charactersIndex == characters.size() && isMatch(subsets, finalSubsets)) {
            solutionMap.put(convertToMapKey(charactersIndex, subsets), 1L);
            return 1L;
        } else if (charactersIndex == characters.size() && !isMatch(subsets, finalSubsets)) {
            solutionMap.put(convertToMapKey(charactersIndex, subsets), 0L);
            return 0L;
        }

        //the number of feasible solutions from this state is equal
        //to the number of feasible solutions if we put a '#' in the next point plus
        //the number of feasible solutions if we put a '.' in the next point
        characters.set(charactersIndex, '#');
        List<Subset> newSolution1Subsets = subsets.stream()
                .map(Subset::new).collect(Collectors.toList());
        long value1 = Solution.solve(characters, charactersIndex,
                newSolution1Subsets, finalSubsets, solutionMap);

        characters.set(charactersIndex, '.');
        List<Subset> newSolution2Subsets = subsets.stream()
                .map(Subset::new).collect(Collectors.toList());
        long value2 = Solution.solve(characters, charactersIndex,
                newSolution2Subsets, finalSubsets, solutionMap);
        characters.set(charactersIndex, '?');

        return value1 + value2;
    }

    private static boolean isMatch(List<Subset> subsets, List<Subset> finalSubsets) {
        if(subsets.size() != finalSubsets.size()) {
            return false;
        }
        for (int i = 0; i < subsets.size(); i++) {
            if(subsets.get(i).getQuantity() != finalSubsets.get(i).getQuantity()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isMatchPossible(List<Subset> subsets, List<Subset> finalSubsets) {
        for (int i = 0; i < subsets.size(); i++) {
            if(!subsets.get(i).isBounded()) {
                break;
            }
            if(subsets.get(i).getQuantity() != finalSubsets.get(i).getQuantity()) {
                return false;
            }
        }

        return true;
    }

    private static String convertToMapKey(Integer charactersIndex, List<Subset> subsets) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(charactersIndex));
        stringBuilder.append(":");
        for(Subset subset : subsets) {
            stringBuilder.append(String.valueOf(subset.getQuantity()));
            stringBuilder.append(".");
        }
        if(!subsets.isEmpty() && !subsets.get(subsets.size() -1).isBounded()) {
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }
}
