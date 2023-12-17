package org.example;

import java.util.Objects;

public class Element {

    private int id;
    private ElementType elementType;

    private int x;

    private int y;

    public Element(int id, ElementType elementType, int x, int y) {
        this.id = id;
        this.elementType = elementType;
        this.x = x;
        this.y = y;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return id == element.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
