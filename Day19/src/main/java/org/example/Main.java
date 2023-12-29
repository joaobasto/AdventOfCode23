package org.example;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 19 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        Map<String, Workflow> workflowByName = new HashMap<>();
        List<Part> parts = new ArrayList<>();
        String line;
        boolean isReadingWorkflows = true;
        while ((line = br.readLine()) != null) {
            if(line.isEmpty()) {
                isReadingWorkflows = false;
                continue;
            }
            if(isReadingWorkflows) {
                String[] characteristics = line.split("[{},]");
                String workflowName = characteristics[0];
                List<Step> steps = new ArrayList<>();
                for (int i = 1; i < characteristics.length - 1; i++) {
                    String destination = characteristics[i].split(":")[1];
                    String label = characteristics[i].split("[<>]")[0];
                    Long value = Long.valueOf(characteristics[i].split("[<>:]")[1]);
                    ConditionType conditionType = characteristics[i].contains(">") ?
                            ConditionType.GREATER_THAN : ConditionType.LESS_THAN;
                    steps.add(new Step(label, value, conditionType, destination));
                }
                String finalDestination = characteristics[characteristics.length - 1];
                workflowByName.put(workflowName, new Workflow(workflowName, finalDestination, steps));
            }
            //if it is reading parts
            else {
                String[] characteristics = line.substring(1).split("[},]");
                Part part = new Part();
                for(String characteristic : characteristics) {
                    String[] characteristicData = characteristic.split("=");
                    String label = characteristicData[0];
                    Long value = Long.valueOf(characteristicData[1]);
                    part.addCharacteristic(label, value);
                }
                parts.add(part);
            }
        }

        Long result = 0L;
        for (Part part : parts) {
            Workflow currentWorkflow = workflowByName.get("in");
            String destination = null;
            while (currentWorkflow != null) {
                destination = currentWorkflow.getDestination(part);
                currentWorkflow = workflowByName.get(destination);
            }
            if(destination.equals("A")) {
                result += part.sumValues();
            }
        }

        System.out.println("The total result is: "+ result);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 19 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        Map<String, Workflow> workflowByName = new HashMap<>();
        List<Part> parts = new ArrayList<>();
        String line;
        boolean isReadingWorkflows = true;
        while ((line = br.readLine()) != null) {
            if(line.isEmpty()) {
                isReadingWorkflows = false;
                continue;
            }
            if(isReadingWorkflows) {
                String[] characteristics = line.split("[{},]");
                String workflowName = characteristics[0];
                List<Step> steps = new ArrayList<>();
                for (int i = 1; i < characteristics.length - 1; i++) {
                    String destination = characteristics[i].split(":")[1];
                    String label = characteristics[i].split("[<>]")[0];
                    Long value = Long.valueOf(characteristics[i].split("[<>:]")[1]);
                    ConditionType conditionType = characteristics[i].contains(">") ?
                            ConditionType.GREATER_THAN : ConditionType.LESS_THAN;
                    steps.add(new Step(label, value, conditionType, destination));
                }
                String finalDestination = characteristics[characteristics.length - 1];
                workflowByName.put(workflowName, new Workflow(workflowName, finalDestination, steps));
            }
            //if it is reading parts
            else {
                String[] characteristics = line.substring(1).split("[},]");
                Part part = new Part();
                for(String characteristic : characteristics) {
                    String[] characteristicData = characteristic.split("=");
                    String label = characteristicData[0];
                    Long value = Long.valueOf(characteristicData[1]);
                    part.addCharacteristic(label, value);
                }
                parts.add(part);
            }
        }

        Workflow currentWorkflow = workflowByName.get("in");

        Map<Map<String, Range>, String> currentRanges = currentWorkflow.getRangeDestinations();

        Map<Map<String, Range>, String> newRanges;

        List<Map<String, Range>> finalRanges = new ArrayList<>();

        while (true) {
            newRanges = new HashMap<>();
            for (Map.Entry<Map<String, Range>, String> currentRange : currentRanges.entrySet()) {
                if (currentRange.getValue().equals("A")) {
                    finalRanges.add(currentRange.getKey());
                    continue;
                }
                newRanges.putAll(
                        workflowByName.get(currentRange.getValue()).calculateRangeDestinations(currentRange.getKey()));
            }
            currentRanges.clear();
            currentRanges.putAll(newRanges);
            if(currentRanges.isEmpty()) {
                break;
            }
        }

        long result = 0L;
        for (Map<String, Range> finalRange : finalRanges) {
            long subResult = 1;
            for (Range range : finalRange.values()) {
                subResult = subResult * (range.getUpperBound() - range.getLowerBound() + 1);
            }
            result += subResult;
        }
        System.out.println("The result is: " + result);
    }


}