package org.example;

import java.util.Objects;

public class Event implements Comparable<Event>{

    Integer time;
    private EventType eventType;
    private Module sendingModule;
    private Module receivingModule;
    private boolean pulse;

    public Event(EventType eventType, Module sendingModule, Module receivingModule, boolean pulse) {
        this.eventType = eventType;
        this.sendingModule = sendingModule;
        this.receivingModule = receivingModule;
        this.pulse = pulse;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTime() {
        return time;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Module getSendingModule() {
        return sendingModule;
    }

    public Module getReceivingModule() {
        return receivingModule;
    }

    public boolean getPulse() {
        return pulse;
    }

    @Override
    public int compareTo(Event event) {
        if(this.time - event.getTime() == 0) {
            return Objects.hash(this) - Objects.hash(event);
        }
        return this.time - event.getTime();
    }
}
