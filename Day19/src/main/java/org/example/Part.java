package org.example;

import java.util.HashMap;
import java.util.Map;

public class Part {

    private Map<String, Long> characteristics;

    public Part() {
        this.characteristics = new HashMap<>();
    }

    public Map<String, Long> getCharacteristics() {
        return characteristics;
    }

    public void addCharacteristic(String label, Long value) {
        characteristics.put(label, value);
    }

    public Long sumValues() {
        return characteristics.values().stream().reduce(0L, Long::sum);
    }
}
