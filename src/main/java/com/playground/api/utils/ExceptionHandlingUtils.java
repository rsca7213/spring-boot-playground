package com.playground.api.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlingUtils {
    public static String getHttpMessageNotReadableMessage(Throwable cause) {
        Map<Class<? extends Throwable>, String> messageMap = new HashMap<>();
        messageMap.put(InvalidFormatException.class, "Invalid data format for one or more fields in the request body");
        messageMap.put(JsonParseException.class, "The request body contains malformed JSON syntax");
        messageMap.put(JsonMappingException.class, "The request body could not be parsed due to a structural or type mismatch");

        // Attempt to find the messageMap match
        String message = messageMap.get(cause.getClass());

        // Fallback if cause is not recognized or specific info isn't available
        if (message.isEmpty() && cause.getMessage() != null) {
            message = "Cannot process the request, ensure it provides valid JSON syntax";
        }

        return message;
    }

    private static String getFieldNameFromThrowableCause(
            Throwable cause
    ) {
        InvalidFormatException ife = (InvalidFormatException) cause;
        if (ife.getPath() != null && !ife.getPath().isEmpty()) {
            JsonMappingException.Reference lastRef = ife.getPath().getLast();
            if (lastRef != null && lastRef.getFieldName() != null) {
                return lastRef.getFieldName();
            }
        }

        return "Unknown";
    }

    public static Map<String, String> getHttpMessageNotReadableErrors(Throwable cause) {
        String fieldName = null;
        String errorMessage = null;

        if (cause instanceof InvalidFormatException ife) {
            fieldName = getFieldNameFromThrowableCause(cause);
            errorMessage = String.format("Value '%s' is not a valid '%s'", ife.getValue(), ife.getTargetType().getSimpleName());
        }
        if (cause instanceof JsonParseException jpe) {
            fieldName = "Unknown";
            errorMessage = "Malformed JSON syntax at line " + jpe.getLocation().getLineNr() + ", column " + jpe.getLocation().getColumnNr();
        }
        if (cause instanceof JsonMappingException jme) {
            fieldName = getFieldNameFromThrowableCause(cause);
            errorMessage = "Input does not match expected structure or type";
        }

        Map<String, String> errors = new HashMap<>();
        errors.put(fieldName, errorMessage);
        return errors;
    }
}
