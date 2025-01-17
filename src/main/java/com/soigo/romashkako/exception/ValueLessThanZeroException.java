package com.soigo.romashkako.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValueLessThanZeroException extends DetailsException {
    public ValueLessThanZeroException(String message, Map<String, String> details) {
        super(message, details);
    }
}
