package org.example;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 20 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        SimulationEngine simulationEngine = new SimulationEngine();
        Map<String, Module> modulesByName = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] preparedData = line.split(" -> ");
            String[] outputs = preparedData[1].split(", ");

            if (preparedData[0].equals("broadcaster")) {
                Broadcaster broadcaster = new Broadcaster(simulationEngine, outputs);
                modulesByName.put(preparedData[0], broadcaster);
            } else if (preparedData[0].startsWith("%")) {
                FlipFlop flipFlop = new FlipFlop(simulationEngine, outputs);
                modulesByName.put(preparedData[0].substring(1), flipFlop);
            } else if (preparedData[0].startsWith("&")) {
                Conjunction conjunction = new Conjunction(simulationEngine, outputs);
                modulesByName.put(preparedData[0].substring(1), conjunction);
            } else {
                Output output = new Output(simulationEngine, outputs);
                modulesByName.put(preparedData[0], output);
            }
        }

        Set<String> missingModules = new HashSet<>();
        for (Module module : modulesByName.values()) {
            for(String destinationModuleName: module.destinationModulesNames) {
                if (modulesByName.get(destinationModuleName) == null) {
                    missingModules.add(destinationModuleName);
                }
            }
        }
        for(String missingModule : missingModules) {
            Output output = new Output(simulationEngine, new String[0]);
            modulesByName.put(missingModule, output);
        }

        for (Module module : modulesByName.values()) {
            for(String destinationModuleName: module.destinationModulesNames) {
                module.addDestinationModule(modulesByName.get(destinationModuleName));
                modulesByName.get(destinationModuleName).addInputModule(module);
            }
        }

        for(int i = 1; i <= 1000; i++) {
            simulationEngine.addEvent(1,
                    new Event(EventType.RECEIVE_SIGNAL, null, modulesByName.get("broadcaster"), false));

            simulationEngine.run();
        }

        System.out.println("The result is: " + simulationEngine.numberOfLowPulses * simulationEngine.numberOfHighPulses);
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 20 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
        }
    }


}