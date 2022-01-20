package com.hpsg;

public class Pair {
    private String key;
    private Object value;

    public Pair(final String key, final Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        if (value.getClass() == String.class) {
            return (key + ": " + "\"" + value + "\"");
        } else {
            return (key + ": " + value);
        }
    }
}
