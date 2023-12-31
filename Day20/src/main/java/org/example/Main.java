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
                Broadcaster broadcaster = new Broadcaster("broadcaster", simulationEngine, outputs);
                modulesByName.put(preparedData[0], broadcaster);
            } else if (preparedData[0].startsWith("%")) {
                FlipFlop flipFlop = new FlipFlop(preparedData[0].substring(1), simulationEngine, outputs);
                modulesByName.put(preparedData[0].substring(1), flipFlop);
            } else if (preparedData[0].startsWith("&")) {
                Conjunction conjunction = new Conjunction(preparedData[0].substring(1), simulationEngine, outputs);
                modulesByName.put(preparedData[0].substring(1), conjunction);
            } else {
                Output output = new Output(preparedData[0], simulationEngine, outputs);
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
            Output output = new Output(missingModule, simulationEngine, new String[0]);
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

    /**
     * This problem is similar to problem 8 in the sense that I couldn't get a programmatic
     * solution that works in a reasonable ammount of time. This method works but takes an unrealistic
     * ammount of time to complete. Let me explain how I solved this:
     * after the experience I had with Day 8, I had a feeling I would need to check the data to see patterns
     * or interpret the circuit to understand how I could get the solution.
     * I started to check if there was a pattern on how the number of low and high pulses of the output changed
     * with each run but I had no success.
     * So I decided to try to draw the circuit and understand when we would get a low pass on the exit.
     * I understood that the output was connected to a single conjunction, this conjunction was fed by four conjunctions.
     * Then I checked that each of these four conjunctions had a chain of flipflops exiting from to it, and some of the chain members were inputs to the
     * connection itself.
     * I could understand by some manual tests that the flipflops would change values intermittently, the more far in the chain,
     * the more intermittent. I had a suspicion the chain was representing a binary number, where each flipflop was a bit,
     * starting the chain on the less significant bit.
     * I started running the program below printing the state of the inputs of one of these connnections and confirmed that
     * the chain represented a binary number. I could also understand by checking the circuit that to have a low pass in the output, we would need
     * to have all entrances of these four conjunctions as high.
     * So, in a paper, I represented for each conjunction, the chain of flip flops and marked the ones that were also entrances
     * to the conjunctions themselves. My suspision was that as long as the flip flops that were entrances were marked to high,
     * the conjunction would pulse a low. I run the program printing each time one of these conjunctions sent a low and found that
     * actually the conjunction would create low pulses in the run with a number equivalent to
     * when the flip flops of the chain it had as input were high and the ones that
     * were not in the input were low. And then it would create again low pulses when the same ammount of runs passed.
     * This showed that each of the four conjunctions would create low pulses with a period equivalent to the number of runs
     * represented by their input flipflops being to true and the ones not in the output being to false, when representing
     * a binary number with the whole flip flop chain.
     * So, to have all the conjunctions sending low pulses I had to calculate the minimum common multiple between these periods of runs.
     * 3943 - conjunction "hm"
     * 4001 - conjunction "pl"
     * 3917 - conjunction "jc"
     * 3947 - conjunction "fd"
     * I calculated the MCM and it was 243902373381257.
     * Now, the thing is, I knew at this number of runs, these conjunctions would create low passes.
     * And I knew that would lead to at least one low pulse created by the output. But I had no evidence it would be ONLY one pulse.
     * Still, the number was quite big so I decided to try to check if it was the answer and it was correct.
     * So, this is not a proper solution: I can't explain exactly why it had to be this value, although it is correct.
     * Still, it was important to leave this explanation in case I decide to go back to this problem in the future to
     * properly solve it.
     */
    private static void exercise2() throws IOException {
        System.out.println("Solving Day 20 Challenge 2: ");

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
                Broadcaster broadcaster = new Broadcaster("broadcaster", simulationEngine, outputs);
                modulesByName.put(preparedData[0], broadcaster);
            } else if (preparedData[0].startsWith("%")) {
                FlipFlop flipFlop = new FlipFlop(preparedData[0].substring(1), simulationEngine, outputs);
                modulesByName.put(preparedData[0].substring(1), flipFlop);
            } else if (preparedData[0].startsWith("&")) {
                Conjunction conjunction = new Conjunction(preparedData[0].substring(1), simulationEngine, outputs);
                modulesByName.put(preparedData[0].substring(1), conjunction);
            } else {
                Output output = new Output(preparedData[0], simulationEngine, outputs);
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
            Output output = new Output(missingModule, simulationEngine, new String[0]);
            modulesByName.put(missingModule, output);
        }

        for (Module module : modulesByName.values()) {
            for(String destinationModuleName: module.destinationModulesNames) {
                module.addDestinationModule(modulesByName.get(destinationModuleName));
                modulesByName.get(destinationModuleName).addInputModule(module);
            }
        }

        Module rxModule = modulesByName.get("rx");
        long numberOfRuns = 0;
        while (true) {
            numberOfRuns++;
            simulationEngine.addEvent(1,
                    new Event(EventType.RECEIVE_SIGNAL, null, modulesByName.get("broadcaster"), false));

            boolean isSuccessful = simulationEngine.runWithCheck(rxModule);
            if (isSuccessful) {
                break;
            }
        }

        System.out.println("Total number of runs was: " + numberOfRuns);
    }


}