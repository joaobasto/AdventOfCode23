package org.example;

import java.util.*;

public class RangeUtils {

    public static Map<String, Range> fullRange = Map.of("x", new Range(1, 4000),
            "m", new Range(1, 4000),
            "a", new Range(1, 4000),
            "s", new Range(1, 4000));

    public static Optional<Map<String, Range>> getOutputRanges(
            Map<String, Range> inputRanges, String label, Long value, ConditionType conditionType) {
        Optional<Range> outputRange = getRangeIntersection(inputRanges.get(label), value, conditionType);
        if (outputRange.isEmpty()) {
            return Optional.empty();
        }
        Map<String, Range> outputRanges = new HashMap<>(inputRanges);
        outputRanges.put(label, outputRange.get());
        return Optional.of(outputRanges);
    }

    private static Optional<Range> getRangeIntersection(Range range, Long value, ConditionType conditionType) {
        if (conditionType == ConditionType.LESS_THAN) {
            int upperBound = (int)Math.min(range.getUpperBound(), value - 1);
            if (upperBound < range.getLowerBound()) {
                return Optional.empty();
            }
            return Optional.of(new Range(range.getLowerBound(), upperBound));
        }
        if (conditionType == ConditionType.GREATER_THAN) {
            int lowerBound = (int)Math.max(range.getLowerBound(), value + 1);
            if (lowerBound > range.getUpperBound()) {
                return Optional.empty();
            }
            return Optional.of(new Range(lowerBound, range.getUpperBound()));
        }
        return Optional.empty();
    }
}
