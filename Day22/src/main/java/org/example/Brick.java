package org.example;

import java.util.ArrayList;
import java.util.List;

public class Brick {

    long posXMin;
    long posXMax;
    long posYMin;
    long posYMax;
    long posZMin;
    long posZMax;

    List<Brick> supportingBricks = new ArrayList<>();

    public Brick(long posXMin, long posYMin, long posZMin, long posXMax, long posYMax, long posZMax) {
        this.posXMin = posXMin;
        this.posXMax = posXMax;
        this.posYMin = posYMin;
        this.posYMax = posYMax;
        this.posZMin = posZMin;
        this.posZMax = posZMax;
    }
}
