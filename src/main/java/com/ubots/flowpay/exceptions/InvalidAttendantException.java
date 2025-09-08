package com.ubots.flowpay.exceptions;

public class InvalidAttendantException extends RuntimeException {
    public InvalidAttendantException(String attendantName) {
        super("Attendant has to have a name and a team");
    }
}
