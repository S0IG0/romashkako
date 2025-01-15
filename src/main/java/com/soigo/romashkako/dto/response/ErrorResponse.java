package com.soigo.romashkako.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {
    private String uri;
    private String message;
    private Map<String, String> details;
}
