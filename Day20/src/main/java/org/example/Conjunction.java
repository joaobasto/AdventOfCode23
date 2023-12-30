package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conjunction extends Module{

    private Map<Module, Boolean> pulseByInputModule;

    public Conjunction(SimulationEngine simulationEngine, String[] destinationModulesNames) {
        super(simulationEngine, destinationModulesNames);
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
}
