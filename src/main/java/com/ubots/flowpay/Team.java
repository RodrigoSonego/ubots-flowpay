package com.ubots.flowpay;

public enum Team {
    CARD,
    LOAN,
    OTHER;

    private final static Team[] values = Team.values();

    public static Team fromInt(int i) {
        return values[i];
    }

    public static boolean isValidOrdinal(int i){
        return i >= 0 && i < values.length;
    }

    public static int toInt(Team team){
        return team.ordinal();
    }
}
