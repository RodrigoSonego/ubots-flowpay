package com.ubots.flowpay.exceptions;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long id) {

        super("Request with id " + id + " not found");
    }
}
