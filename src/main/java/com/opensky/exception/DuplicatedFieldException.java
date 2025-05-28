package com.opensky.exception;

public class DuplicatedFieldException extends OpenSkyException {


    public DuplicatedFieldException(String fieldName, String fieldValue) {
        super("The field '" + fieldName + "' with value '" + fieldValue + "' already exists.");
    }

}
