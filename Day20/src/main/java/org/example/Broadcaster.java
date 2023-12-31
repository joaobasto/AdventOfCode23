package org.example;

public class Broadcaster extends Module {

    public Broadcaster(String name, SimulationEngine simulationEngine, String[] destinationModulesNames) {
        super(name, simulationEngine, destinationModulesNames);
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
