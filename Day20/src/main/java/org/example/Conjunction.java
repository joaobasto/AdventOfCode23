package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conjunction extends Module{

    private Map<Module, Boolean> pulseByInputModule;

    public Conjunction(String name, SimulationEngine simulationEngine, String[] destinationModulesNames) {
        super(name, simulationEngine, destinationModulesNames);
        pulseByInputModule = new HashMap<>();
    }

    @Override
    public void addInputModule(Module inputModule) {
        super.addInputModule(inputModule);
        pulseByInputModule.put(inputModule, false);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType() == EventType.RECEIVE_SIGNAL) {
            pulseByInputModule.put(event.getSendingModule(), event.getPulse());
            boolean areAllPulsesHigh = pulseByInputModule.values().stream()
                    .allMatch(pulse -> pulse.equals(true));
            if (areAllPulsesHigh) {
                for (Module module : destinationModules) {
                    simulationEngine.addEvent(1,
                            new Event(EventType.RECEIVE_SIGNAL, this, module, false));
                }
            } else {
                for (Module module : destinationModules) {
                    simulationEngine.addEvent(1,
                            new Event(EventType.RECEIVE_SIGNAL, this, module, true));
                }
            }
        }
    }

    public void printPulseByInput() {
        for(Map.Entry<Module, Boolean> entry : pulseByInputModule.entrySet()) {
            System.out.print(entry.getKey().name + " - " + entry.getValue() + " ");
        }
        System.out.print("\n");
    }
}
