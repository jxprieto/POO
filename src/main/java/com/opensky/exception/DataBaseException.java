package com.opensky.exception;

public class DataBaseException extends OpenSkyException{
    public DataBaseException(String message, Exception cause) {
        super(message);
        this.initCause(cause);
    }
}
