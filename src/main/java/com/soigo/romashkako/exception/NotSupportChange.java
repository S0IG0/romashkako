package com.soigo.romashkako.exception;

import java.util.Map;

public class NotSupportChange extends DetailsException {
    public NotSupportChange(String message, Map<String, String> details) {
        super(message, details);
    }
}
