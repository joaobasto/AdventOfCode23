package org.example;

public class FlipFlop extends Module{

    boolean state;

    public FlipFlop(SimulationEngine simulationEngine, String[] destinationModulesNames) {
        super(simulationEngine, destinationModulesNames);
        state = false;
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType() == EventType.RECEIVE_SIGNAL) {
            if (!event.getPulse()) {
                state = !state;
                for (Module module : destinationModules) {
                    simulationEngine.addEvent(1,
                            new Event(EventType.RECEIVE_SIGNAL, this, module, state));
                }
            }
        }
    }
}
