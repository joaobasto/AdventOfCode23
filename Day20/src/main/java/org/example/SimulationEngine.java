package org.example;

import java.util.*;

public class SimulationEngine {

    List<Event> events = new ArrayList<>();

    Integer currentTime = 0;

    long numberOfLowPulses = 0L;
    long numberOfHighPulses = 0L;

    public void addEvent(Integer delay, Event event) {
        event.setTime(currentTime + delay);
        events.add(event);
    }


    public void run() {
        while (!events.isEmpty()) {
            Event event = events.remove(0);
            currentTime = event.getTime();
            if (event.getPulse()) {
                numberOfHighPulses++;
            } else {
                numberOfLowPulses++;
            }
            event.getReceivingModule().handleEvent(event);
        }
    }
}
