package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Workflow {

    private String name;

    /** this string represents the destination of parts that do not pass any of the steps
     * can either be the name of another workflow or "A" if accepted or "R" if rejected
     */
    private String finalDestination;

    private List<Step> steps;

    private Map<Map<String, Range>, String> rangeDestinations;

    public Workflow(String name, String finalDestination, List<Step> steps) {
        this.name = name;
        this.finalDestination = finalDestination;
        this.steps = steps;
        this.rangeDestinations = calculateRangeDestinations(RangeUtils.fullRange);
    }

    public String getDestination(Part part) {
        for (Step step: steps) {
            if (step.test(part)) {
                return step.getDestination();
            }
        }
        return finalDestination;
    }

    /**
     * This method will return a map that associates each combination of ranges of values of the different labels
     * (represented by a map of strings - labels - to the respective range) to the string representing the name of the
     * destination workflow or "A" if accepted (if the destination is being rejected then it is ignored).
     * @return
     */
    public Map<Map<String, Range>, String> calculateRangeDestinations(Map<String, Range> currentRanges) {
        Map<Map<String, Range>, String> result = new HashMap<>();
        Optional<Map<String, Range>> optionalCurrentRanges = null;
        for (Step step : steps) {
            Optional<Map<String, Range>> newRanges = step.getValidRange(currentRanges);
            newRanges.ifPresent(stringRangeMap -> {
                if (!step.getDestination().equals("R")) {
                    result.put(stringRangeMap, step.getDestination());
                }
            });
            optionalCurrentRanges = step.getInvalidRange(currentRanges);
            if(optionalCurrentRanges.isPresent()) {
                currentRanges = optionalCurrentRanges.get();
            } else {
                break;
            }
        }
        if(optionalCurrentRanges.isPresent()) {
            currentRanges = optionalCurrentRanges.get();
            if (!finalDestination.equals("R")) {
                result.put(currentRanges, finalDestination);
            }
        }
        return result;
    }

    public Map<Map<String, Range>, String> getRangeDestinations() {
        return rangeDestinations;
    }
}
