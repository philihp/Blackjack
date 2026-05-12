package com.philihp.bj;

public enum Card {
    _2(2),
    _3(3),
    _4(4),
    _5(5),
    _6(6),
    _7(7),
    _8(8),
    _9(9),
    _T(10),
    _A(11);

    private final int value;

    Card(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name().substring(1);
    }

    public int getTableOrdinal() {
        return value - 2;
    }
}
