package com.dp_ua.JogJourney.strava;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

@Slf4j
public class StravaTokenTest {
    public static final String WRONG_DATE_FORMAT = "wrong date format";
    public static final int HOUR = 3600;
    private String token;

    @Before
    public void setUp() {
        token = "token";
    }

    @Test
    public void tokenShouldBeExpired() {
        StravaToken token = getExpiredToken();
        assertTrue(token.isExpired());
    }

    @Test
    public void tokenShouldNotBeExpired() {
        StravaToken token = getNotExpiredToken();
        assertFalse(token.isExpired());
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void tokenShouldThrowException() {
        StravaToken stravaToken = new StravaToken(token, WRONG_DATE_FORMAT);
        stravaToken.isExpired();
        fail();
    }

    private StravaToken getExpiredToken() {
        Instant date = Instant.now().minusSeconds(HOUR);
        log.info("Expired date: " + date);
        return new StravaToken(token, date.toString());
    }

    private StravaToken getNotExpiredToken() {
        Instant date = Instant.now().plusSeconds(HOUR);
        log.info("Not expired date: " + date);
        return new StravaToken(token, date.toString());
    }
}