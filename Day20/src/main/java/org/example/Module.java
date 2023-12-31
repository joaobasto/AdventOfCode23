package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    String name;
    List<Module> inputModules;
    List<Module> destinationModules;
    SimulationEngine simulationEngine;
    String[] destinationModulesNames;

    public Module(String name, SimulationEngine simulationEngine, String[] destinationModulesNames) {
        this.name = name;
        inputModules = new ArrayList<>();
        destinationModules = new ArrayList<>();
        this.simulationEngine = simulationEngine;
        this.destinationModulesNames = destinationModulesNames;
    }

    public String[] getDestinationModulesNames() {
        return destinationModulesNames;
    }

    public void addDestinationModule(Module destinationModule) {
        this.destinationModules.add(destinationModule);
    }

    public void addInputModule(Module inputModule) {
        this.inputModules.add(inputModule);
    }

    public abstract void handleEvent(Event event);
}
