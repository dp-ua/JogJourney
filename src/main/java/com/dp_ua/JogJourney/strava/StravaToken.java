package com.dp_ua.JogJourney.strava;

import lombok.Getter;

import java.time.Instant;

@Getter
public class StravaToken {
    private final String token;
    //example: expires at: 2024-03-06T18:46:40Z
    private final String expiresAt;

    public StravaToken(String token, String expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        Instant now = Instant.now();
        Instant expires = Instant.parse(expiresAt);
        return expires.isBefore(now);
    }
}
