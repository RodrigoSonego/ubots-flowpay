package com.ubots.flowpay.exceptions;

public class AttendantNotFoundException extends RuntimeException {
    public AttendantNotFoundException(int id) {
        super("Attendant with id " + id + " not found");
    }
}
