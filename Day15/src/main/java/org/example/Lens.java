package org.example;

public class Lens {

    private String label;

    private int focalStrength;

    public Lens(String label, int focalStrength) {
        this.label = label;
        this.focalStrength = focalStrength;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getFocalStrength() {
        return focalStrength;
    }

    public void setFocalStrength(int focalStrength) {
        this.focalStrength = focalStrength;
    }
}
