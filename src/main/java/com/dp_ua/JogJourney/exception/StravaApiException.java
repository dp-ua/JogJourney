package com.dp_ua.JogJourney.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class StravaApiException extends Exception {
    @Getter
    private final long code;
    //  401 - Unauthorized
    //  403 - Forbidden
    //  404 - Not Found
    //  429 - Too Many Requests

    public StravaApiException(String message, long code) {
        super(message);
        this.code = code;
    }
}
