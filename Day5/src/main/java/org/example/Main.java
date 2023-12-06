package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 5 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        line = br.readLine();
        String seedsString = line.split(": ")[1];
        List<Long> seeds = Arrays.stream(seedsString.split(" ")).map(Long::valueOf).collect(Collectors.toList());

        List<Set<Range>> rangeSetList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.endsWith("map:")) {
                rangeSetList.add(new HashSet<>());
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) {
                        break;
                    }
                    List<Long> lineInfo = Arrays.stream(line.split(" ")).map(Long::valueOf).collect(Collectors.toList());
                    rangeSetList.get(rangeSetList.size() - 1).add(
                            new Range(
                                    lineInfo.get(1),
                                    lineInfo.get(1) + lineInfo.get(2) - 1,
                                    lineInfo.get(0) - lineInfo.get(1)
                            )
                    );
                }
            }
        }

        List<Long> locations = determineLocations(seeds, rangeSetList);
        long minimumLocation = locations.stream().mapToLong(v -> v).min().getAsLong();
        System.out.println("Minimum location is: " + minimumLocation);
    }

    private static List<Long> determineLocations(List<Long> seeds, List<Set<Range>> rangeSetList) {
        return seeds.stream()
                .map(seed -> {
                    long mappedNumber = seed;
                    for (Set<Range> rangeSet : rangeSetList) {
                        for (Range range : rangeSet) {
                            if (range.containsNumber(mappedNumber)) {
                                mappedNumber = range.applyOffset(mappedNumber);
                                break;
                            }
                        }
                    }
                    return mappedNumber;
                })
                .collect(Collectors.toList());
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 5 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        line = br.readLine();
        String seedsString = line.split(": ")[1];
        Set<Range> initialRanges = new HashSet<>();
        List<Long> seedsData = Arrays.stream(seedsString.split(" ")).map(Long::valueOf).collect(Collectors.toList());
        int i = 0;
        while (i < seedsData.size() - 1) {
            initialRanges.add(new Range(seedsData.get(i), seedsData.get(i) + seedsData.get(i + 1) - 1, 0L));
            i = i +2;
        }

        List<Set<Range>> rangeSetList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.endsWith("map:")) {
                rangeSetList.add(new HashSet<>());
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) {
                        break;
                    }
                    List<Long> lineInfo = Arrays.stream(line.split(" ")).map(Long::valueOf).collect(Collectors.toList());
                    rangeSetList.get(rangeSetList.size() - 1).add(
                            new Range(
                                    lineInfo.get(1),
                                    lineInfo.get(1) + lineInfo.get(2) - 1,
                                    lineInfo.get(0) - lineInfo.get(1)
                            )
                    );
                }
            }
        }

        long minimumLocation = determineMinimumLocation(initialRanges, rangeSetList);
        System.out.println("Minimum location is: " + minimumLocation);
    }

    private static Long determineMinimumLocation(Set<Range> initialRanges, List<Set<Range>> rangeSetList) {
        Set<Range> nonOffsetAppliedRanges = initialRanges;
        Set<Range> offsetAppliedRanges = new HashSet<>();
        Set<Range> newNonOffsetAppliedRanges = new HashSet<>();
        for(Set<Range> rangeSet : rangeSetList) {
            for(Range range : rangeSet) {
                for(Range nonOffsetAppliedRange : nonOffsetAppliedRanges) {
                    range.applyOffsetToRange(nonOffsetAppliedRange).ifPresent(offsetAppliedRanges::add);
                    range.getNonIntersectingRange(nonOffsetAppliedRange).ifPresent(newNonOffsetAppliedRanges::addAll);
                }
                nonOffsetAppliedRanges = new HashSet<>(newNonOffsetAppliedRanges);
                newNonOffsetAppliedRanges = new HashSet<>();
            }
            nonOffsetAppliedRanges.addAll(offsetAppliedRanges);
            offsetAppliedRanges = new HashSet<>();
        }
        return nonOffsetAppliedRanges.stream()
                .map(range -> range.startPos).mapToLong(v -> v).min().getAsLong();
    }
}
