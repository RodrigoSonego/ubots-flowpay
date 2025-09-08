package com.ubots.flowpay.exceptions;

import com.ubots.flowpay.RequestStatus;

public class InvalidRequestStatusException extends RuntimeException {
    public InvalidRequestStatusException(RequestStatus requestStatus) {

        super("Invalid request status for operation, current status: " + requestStatus);
    }
}
