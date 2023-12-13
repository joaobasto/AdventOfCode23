package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

    private List<Character> characters;
    private int charactersIndex;
    private List<Subset> subsets;

    private Map<Integer, Map<List<Subset>, Long>> solutionMap = new HashMap<>();

    public Solution(List<Character> characters) {
        this.characters = characters;
        this.charactersIndex = 0;
        this.subsets = new ArrayList<>();
    }

    public Solution(List<Character> characters, int charactersIndex, List<Subset> subsets) {
        this.characters = characters;
        this.charactersIndex = charactersIndex;
        this.subsets = subsets;
    }

    /**
     * Calculates number of feasible solutions that can be obtained.
     * @return
     */
    public long solve(List<Subset> finalSubsets) {
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
        if (solutionMap.get(charactersIndex) != null &&
                solutionMap.get(charactersIndex).get(subsets) != null) {
            return solutionMap.get(charactersIndex).get(subsets);
        }

        //check if this is a terminal state:
        //unfeasible state:
        //we have more points than what is possible in total
        long currentTotalPoints = subsets.stream().mapToLong(Subset::getQuantity).sum();
        long finalTotalPoints = finalSubsets.stream().mapToLong(Subset::getQuantity).sum();
        if(currentTotalPoints > finalTotalPoints) {
            solutionMap.computeIfAbsent(charactersIndex, unused -> new HashMap<>());
            solutionMap.get(charactersIndex).put(subsets, 0L);
            return 0L;
        }
        //we have less maximum possible points than the total points
        long maximumTotalPoints = currentTotalPoints +
                characters.subList(charactersIndex, characters.size()).stream()
                        .filter(character -> character.equals('?') || character.equals('#'))
                        .count();
        if(maximumTotalPoints < finalTotalPoints) {
            return 0L;
        }
        //or our current sets will never be able to match the final sets
        if (!isMatchPossible(subsets, finalSubsets)) {
            solutionMap.computeIfAbsent(charactersIndex, unused -> new HashMap<>());
            solutionMap.get(charactersIndex).put(subsets, 0L);
            return 0L;
        }
        //or because it is a feasible solution (we reached the end of the characters
        //and we have the sets of points equal to the desired sets of points)
        if (charactersIndex == characters.size() && isMatch(subsets, finalSubsets)) {
            solutionMap.computeIfAbsent(charactersIndex, unused -> new HashMap<>());
            solutionMap.get(charactersIndex).put(subsets, 1L);
            return 1L;
        } else if (charactersIndex == characters.size() && !isMatch(subsets, finalSubsets)) {
            solutionMap.computeIfAbsent(charactersIndex, unused -> new HashMap<>());
            solutionMap.get(charactersIndex).put(subsets, 0L);
            return 0L;
        }

        //the number of feasible solutions from this state is equal
        //to the number of feasible solutions if we put a '#' in the next point plus
        //the number of feasible solutions if we put a '.' in the next point
        List<Character> newSolution1Characters = new ArrayList<>(characters);
        newSolution1Characters.set(charactersIndex, '#');
        List<Subset> newSolution1Subsets = subsets.stream()
                .map(Subset::new).collect(Collectors.toList());
        Solution newSolution1 = new Solution(newSolution1Characters, charactersIndex, newSolution1Subsets);
        long value1 = newSolution1.solve(finalSubsets);

        List<Character> newSolution2Characters = new ArrayList<>(characters);
        newSolution2Characters.set(charactersIndex, '.');
        List<Subset> newSolution2Subsets = subsets.stream()
                .map(Subset::new).collect(Collectors.toList());
        Solution newSolution2 = new Solution(newSolution2Characters, charactersIndex, newSolution2Subsets);
        long value2 = newSolution2.solve(finalSubsets);

        return value1 + value2;
    }

    private boolean isMatch(List<Subset> subsets, List<Subset> finalSubsets) {
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

    private boolean isMatchPossible(List<Subset> subsets, List<Subset> finalSubsets) {
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
}
