package org.example;

import java.io.*;
import java.util.*;


public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        //no exercise 2 in day 25
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 25 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        Set<Component> components = new HashSet<>();
        Set<Link> links = new HashSet<>();
        Map<String, Integer> idByComponentName = new HashMap<>();
        Map<Integer, String> componentNameById = new HashMap<>();
        int nextId = 0;
        while ((line = br.readLine()) != null) {
            String[] lineData = line.split(": ");
            String[] linkedComponentsNames = lineData[1].split(" ");
            if(!components.contains(new Component(nextId, lineData[0]))) {
                components.add(new Component(nextId, lineData[0]));
                idByComponentName.put(lineData[0], nextId);
                componentNameById.put(nextId, lineData[0]);
                nextId++;
            }
            for(String linkedComponentName : linkedComponentsNames) {
                if(!components.contains(new Component(nextId, linkedComponentName))) {
                    components.add(new Component(nextId, linkedComponentName));
                    idByComponentName.put(linkedComponentName, nextId);
                    componentNameById.put(nextId, linkedComponentName);
                    nextId++;
                }
                links.add(new Link(List.of(idByComponentName.get(lineData[0]), idByComponentName.get(linkedComponentName))));
            }
        }

        int debug = 1;

        Map<Integer, List<String>> componentsMergedByComponentId = new HashMap<>();
        while (true) {
            Map<Integer, List<Integer>> graph = new HashMap<>();
            for(Component component: components) {
                Integer componentId = idByComponentName.get(component.name);
                List<Integer> connectedIds = new ArrayList<>();
                for(Link link : links) {
                    if (link.componentIds.get(0).equals(componentId)) {
                        connectedIds.add(link.componentIds.get(1));
                    }
                    if (link.componentIds.get(1).equals(componentId)) {
                        connectedIds.add(link.componentIds.get(0));
                    }
                }
                graph.put(idByComponentName.get(component.name), connectedIds);
            }

            componentsMergedByComponentId = new HashMap<>();
            for(Map.Entry<Integer, String> entry : componentNameById.entrySet()) {
                List<String> componentNames = new ArrayList<>();
                componentNames.add(entry.getValue());
                componentsMergedByComponentId.put(entry.getKey(), componentNames);
            }

            int minCut = kargerMinCut(new HashMap<>(graph), componentsMergedByComponentId);
            if(minCut == 6) {
                break;
            }
        }

        long result = 1;
        for(List<String> componentNames : componentsMergedByComponentId.values()) {
            result = result * (long) componentNames.size();
        }

        System.out.println("The result is: " + result);
    }

    public static int kargerMinCut(Map<Integer, List<Integer>> graph,
                                   Map<Integer, List<String>> componentsMergedByComponentId) {

        while (graph.size() > 2) {
            // Choose a random edge
            List<Integer> vertices = new ArrayList<>(graph.keySet());
            int randomVertexIndex = new Random().nextInt(vertices.size());
            int randomVertex = vertices.get(randomVertexIndex);
            List<Integer> edges = graph.get(randomVertex);
            int randomEdgeIndex = new Random().nextInt(edges.size());
            int randomNeighbor = edges.get(randomEdgeIndex);

            // Merge the two vertices
            List<Integer> mergedEdges = graph.get(randomNeighbor);
            for (int edge : mergedEdges) {
                if (edge != randomVertex) {
                    edges.add(edge);
                    // Update references in other vertices' adjacency lists
                    List<Integer> otherEdges = graph.get(edge);
                    for (int i = 0; i < otherEdges.size(); i++) {
                        if (otherEdges.get(i) == randomNeighbor) {
                            otherEdges.set(i, randomVertex);
                        }
                    }
                }
            }
            //update the structure that will hold the names of the components held by the merged vertex
            List<String> names = componentsMergedByComponentId.get(randomNeighbor);
            componentsMergedByComponentId.get(randomVertex).addAll(names);
            componentsMergedByComponentId.remove(randomNeighbor);

            // Remove self-loops
            edges.removeIf(edge -> edge == randomNeighbor);

            // Remove the merged vertex
            graph.remove(randomNeighbor);
        }

        // Count remaining edges to find the min cut
        int minCut = 0;
        for (List<Integer> edges : graph.values()) {
            minCut += edges.size();
        }

        return minCut;
    }
}