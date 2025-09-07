package com.ubots.flowpay;

public enum Team {
    CARD,
    LOAN,
    OTHER;

    private final static Team[] values = Team.values();

    public static Team fromInt(int i) {
        return values[i];
    }
}
