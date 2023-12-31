package org.example;

public class Output extends Module{

    public Output(String name, SimulationEngine simulationEngine, String[] destinationModulesNames) {
        super(name, simulationEngine, destinationModulesNames);
    }

    @Override
    public void handleEvent(Event event) {

    }
}
