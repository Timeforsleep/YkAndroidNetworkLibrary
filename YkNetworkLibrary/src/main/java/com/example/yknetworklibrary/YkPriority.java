package com.example.yknetworklibrary;

public enum YkPriority {

    LOW(0),
    NORMAL(1),
    HIGH(2),
    IMMEDIATE(3);

    private final int value;

    YkPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // 比较优先级大小
}
