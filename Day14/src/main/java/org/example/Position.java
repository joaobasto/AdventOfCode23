package org.example;

public class Position {

    private ElementType elementType;

    public Position(ElementType elementType) {
        this.elementType = elementType;
    }


    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }
}
