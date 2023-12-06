package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Range {

    Long startPos; //inclusive start position
    Long endPos; //inclusive end position
    Long offsetToApply;

    public Range(Long startPos, Long endPos, Long offsetToApply) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.offsetToApply = offsetToApply;
    }

    public boolean containsNumber(Long number) {
        if (number >= startPos && number <= endPos) {
            return true;
        }
        return false;
    }

    public long applyOffset(Long number) {
        return number + offsetToApply;
    }

    /**
     * Checks if input range intersects with this range and, if so, returns a new range consisting of the intersection
     * plus offset application. Otherwise, returns an empty optional.
     * @param range
     * @return
     */
    public Optional<Range> applyOffsetToRange(Range range) {
        long intersectionStart = Math.max(range.startPos, this.startPos);
        long intersectionEnd = Math.min(range.endPos, this.endPos);
        if(intersectionStart > intersectionEnd) {
            return Optional.empty();
        }
        return Optional.of(new Range(
                intersectionStart + offsetToApply,
                intersectionEnd + offsetToApply,
                0L));
    }

    public Optional<List<Range>> getNonIntersectingRange(Range range) {
        long intersectionStart = Math.max(range.startPos, this.startPos);
        long intersectionEnd = Math.min(range.endPos, this.endPos);
        if(intersectionStart > intersectionEnd) {
            return Optional.of(List.of(range));
        }
        List<Range> ranges = new ArrayList<>();
        if(intersectionStart > range.startPos) {
            ranges.add(new Range(range.startPos, intersectionStart - 1, 0L));
        }
        if(intersectionEnd < range.endPos) {
            ranges.add(new Range(intersectionEnd + 1, range.endPos, 0L));
        }
        return Optional.of(ranges);
    }
}
