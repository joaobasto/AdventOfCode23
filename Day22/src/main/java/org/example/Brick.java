package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Brick {

    long posXMin;
    long posXMax;
    long posYMin;
    long posYMax;
    long posZMin;
    long posZMax;

    Set<Brick> supportingBricks = new HashSet<>();

    public Brick(long posXMin, long posYMin, long posZMin, long posXMax, long posYMax, long posZMax) {
        this.posXMin = posXMin;
        this.posXMax = posXMax;
        this.posYMin = posYMin;
        this.posYMax = posYMax;
        this.posZMin = posZMin;
        this.posZMax = posZMax;
    }
}
