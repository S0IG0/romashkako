package com.soigo.romashkako.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class DetailsException extends RuntimeException {
    private final Map<String, String> details;
    public DetailsException(String message, Map<String, String> details) {
        super(message);
        this.details = details;
    }
}
