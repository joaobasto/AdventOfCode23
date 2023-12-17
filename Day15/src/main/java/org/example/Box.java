package org.example;

import java.util.LinkedHashMap;

public class Box {

    private long id;

    private LinkedHashMap<String, Lens> lensByLabel;

    public Box(long id) {
        this.id = id;
        this.lensByLabel = new LinkedHashMap<>();
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LinkedHashMap<String, Lens> getLensByLabel() {
        return lensByLabel;
    }

    public void setLensByLabel(LinkedHashMap<String, Lens> lensByLabel) {
        this.lensByLabel = lensByLabel;
    }
}
