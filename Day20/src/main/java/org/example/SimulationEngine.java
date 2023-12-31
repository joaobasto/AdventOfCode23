package org.example;

import java.util.*;

public class SimulationEngine {

    List<Event> events = new LinkedList<>();

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

    /**
     * This method runs the simulation just like method run(), but checks if a single
     * low pulse was sent to checkedModule and returns true if that's the case, and false otherwise
     * @return
     */
    public boolean runWithCheck(Module checkedModule) {
        long countLowPulseRx = 0;
        long countHighPulseRx = 0;
        while (!events.isEmpty()) {
            Event event = events.remove(0);
            currentTime = event.getTime();
            if (event.getPulse()) {
                numberOfHighPulses++;
            } else {
                numberOfLowPulses++;
            }
            if(event.getReceivingModule() == checkedModule) {
                if (event.getPulse()) {
                    countHighPulseRx++;
                } else {
                    countLowPulseRx++;
                }
            }
            event.getReceivingModule().handleEvent(event);
        }
        return countHighPulseRx == 0 && countLowPulseRx == 1;
    }
}
