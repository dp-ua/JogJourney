package com.dp_ua.JogJourney.strava.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StravaTokenTest {

    private StravaToken token;

    @BeforeEach
    public void setUp() {
        token = new StravaToken();
    }

    @Test
    public void shouldBeNotExpired() {
        Instant now = Instant.now();
        token.setExpiresAt(now.toEpochMilli() + 1000);
        assertFalse(token.isExpired());
    }

    @Test
    public void shouldBeExpired() {
        Instant now = Instant.now();
        token.setExpiresAt(now.toEpochMilli() - 1000);
        assertTrue(token.isExpired());
    }
}