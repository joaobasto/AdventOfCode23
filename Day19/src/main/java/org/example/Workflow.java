package org.example;

import java.util.List;

public class Workflow {

    private String name;

    /** this string represents the destination of parts that do not pass any of the steps
     * can either be the name of another workflow or "A" if accepted or "R" if rejected
     */
    private String finalDestination;

    private List<Step> steps;

    public Workflow(String name, String finalDestination, List<Step> steps) {
        this.name = name;
        this.finalDestination = finalDestination;
        this.steps = steps;
    }

    public String getDestination(Part part) {
        for (Step step: steps) {
            if (step.test(part)) {
                return step.getDestination();
            }
        }
        return finalDestination;
    }
}
