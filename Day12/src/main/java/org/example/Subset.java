package org.example;

import java.util.Objects;

public class Subset {

    private long quantity;
    private boolean bounded;

    public Subset(long quantity, boolean bounded) {
        this.quantity = quantity;
        this.bounded = bounded;
    }

    public Subset(Subset subset) {
        this.quantity = subset.getQuantity();
        this.bounded = subset.isBounded();
    }

    public long getQuantity() {
        return quantity;
    }

    public boolean isBounded() {
        return bounded;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setBounded(boolean bounded) {
        this.bounded = bounded;
    }

    public void increment() {
        this.quantity += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subset subset = (Subset) o;
        return quantity == subset.quantity && bounded == subset.bounded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, bounded);
    }
}
