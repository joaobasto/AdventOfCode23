package org.example;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Link {

    List<Integer> componentIds;

    public Link(List<Integer> componentIds) {
        this.componentIds = componentIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(componentIds, link.componentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentIds);
    }
}
