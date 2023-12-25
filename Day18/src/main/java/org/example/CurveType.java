package org.example;

public enum CurveType {
    UP_LEFT,
    UP_RIGHT,
    DOWN_LEFT,
    DOWN_RIGHT;

    public boolean invertsState(CurveType previousCurve) {
        if(this == UP_LEFT) {
            if (previousCurve == UP_RIGHT) {
                return false;
            }
            if (previousCurve == DOWN_RIGHT) {
                return true;
            }
        }
        if(this == DOWN_LEFT) {
            if (previousCurve == UP_RIGHT) {
                return true;
            }
            if (previousCurve == DOWN_RIGHT) {
                return false;
            }
        }
        return false;
    }
}
