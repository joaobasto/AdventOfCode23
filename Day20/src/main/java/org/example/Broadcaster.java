package org.example;

public class Broadcaster extends Module {

    public Broadcaster(SimulationEngine simulationEngine, String[] destinationModulesNames) {
        super(simulationEngine, destinationModulesNames);
    }
    @Override
    public void handleEvent(Event event) {
        if (event.getEventType() == EventType.RECEIVE_SIGNAL) {
            for (Module module : destinationModules) {
                simulationEngine.addEvent(1,
                        new Event(EventType.RECEIVE_SIGNAL, this, module, event.getPulse()));
            }
        }
    }
}
