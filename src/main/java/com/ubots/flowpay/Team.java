package com.ubots.flowpay;

public enum Team {
    CARD,
    LOAN,
    OTHER;

    public static Team fromInt(int i) {
        return Team.values()[i];
    }
}
