package org.example;

public enum ElementType {

    EMPTY,
    ROUNDED_ROCK,
    CUBED_ROCK;

    public static ElementType fromCharacter(Character c) {
        if(c.equals('O')) {
            return ROUNDED_ROCK;
        } else if(c.equals('#')) {
            return CUBED_ROCK;
        } else {
            return EMPTY;
        }
    }
}
