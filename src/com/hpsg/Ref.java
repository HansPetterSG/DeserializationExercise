package com.hpsg;

public class Ref {
    private float from;
    private float to;

    public Ref(final float from, final float to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return (from + "-" + to);
    }
}
