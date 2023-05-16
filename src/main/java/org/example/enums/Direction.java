package org.example.enums;

public enum Direction {
    N(1),
    W(2);

    final int i;

    Direction(int numb) {
        this.i = numb;
    }

    public int getI() {
        return i;
    }
}
