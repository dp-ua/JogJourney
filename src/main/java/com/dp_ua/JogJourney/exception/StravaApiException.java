package com.dp_ua.JogJourney.exception;

import lombok.Getter;

public class StravaApiException extends Exception {
    @Getter
    private long code;
    //  401 - Unauthorized
    //  403 - Forbidden
    //  404 - Not Found
    //  429 - Too Many Requests

    public StravaApiException(String message, long code) {
        super(message);
        this.code = code;
    }
}
