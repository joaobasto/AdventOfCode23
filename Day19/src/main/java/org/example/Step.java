package org.example;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;

public class Step {

    /** label that will be tested in this step
     *
     */
    private String label;

    /**
     * Value to be used in the test in this step
     */
    private Long value;

    /** Type of condition to be used in this step
     *
     */
    private ConditionType conditionType;

    /** this string represents the destination of parts that pass this step
     * can either be the name of another workflow or "A" if accepted or "R" if rejected
     */
    private String destination;

    public Step(String label, Long value, ConditionType conditionType, String destination) {
        this.label = label;
        this.value = value;
        this.conditionType = conditionType;
        this.destination = destination;
    }

    public boolean test(Part part) {
        if (conditionType == ConditionType.LESS_THAN) {
            return part.getCharacteristics().get(label) < value;
        } else {
            return part.getCharacteristics().get(label) > value;
        }
    }

    public Optional<Map<String, Range>> getValidRange(Map<String, Range> inputRanges) {
        return RangeUtils.getOutputRanges(inputRanges, label, value, conditionType);
    }

    public Optional<Map<String, Range>> getInvalidRange(Map<String, Range> inputRanges) {
        if(conditionType == ConditionType.LESS_THAN) {
            return RangeUtils.getOutputRanges(inputRanges, label, value - 1, ConditionType.GREATER_THAN);
        }
        if(conditionType == ConditionType.GREATER_THAN) {
            return RangeUtils.getOutputRanges(inputRanges, label, value + 1, ConditionType.LESS_THAN);
        }
        return Optional.empty();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
