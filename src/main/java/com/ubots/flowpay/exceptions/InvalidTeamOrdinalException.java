package com.ubots.flowpay.exceptions;

public class InvalidTeamOrdinalException extends RuntimeException {
    public InvalidTeamOrdinalException(int ordinal) {
        super("No team with ordinal " + ordinal + " exists");
    }
}
