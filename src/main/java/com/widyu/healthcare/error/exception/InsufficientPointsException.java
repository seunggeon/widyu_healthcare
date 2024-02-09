package com.widyu.healthcare.error.exception;

import java.io.IOException;

public class InsufficientPointsException extends IOException {

    public InsufficientPointsException(String message) {
        super(message);
    }
}