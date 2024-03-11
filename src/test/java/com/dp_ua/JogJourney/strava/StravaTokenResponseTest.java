package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import com.dp_ua.JogJourney.strava.entity.StravaTokenResponse;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StravaTokenResponseTest {
    @Test
    public void shouldParseBody_WithAthlete() {
        StravaTokenResponse response = new StravaTokenResponse("{\"token_type\":\"Bearer\",\"expires_at\":1710125802,\"expires_in\":18621,\"refresh_token\":\"4c528f5fe...\",\"access_token\":\"8e124b843...\",\"athlete\":{\"id\":1234567,\"username\":\"username\",\"resource_state\":2,\"firstname\":\"firstName\",\"lastname\":\"lastName\",\"bio\":\"\",\"city\":\"City\",\"state\":\"\",\"country\":\"Country\",\"sex\":\"M\",\"premium\":false,\"summit\":false,\"created_at\":\"2020-07-20T09:25:54Z\",\"updated_at\":\"2023-10-21T08:04:52Z\",\"badge_type_id\":0,\"weight\":89.0,\"profile_medium\":\"https://dgalywyr863hv.cloudfront.net/pictures/athletes/64265136/27242468/1/medium.jpg\",\"profile\":\"https://dgalywyr863hv.cloudfront.net/pictures/athletes/64265136/27242468/1/large.jpg\",\"friend\":null,\"follower\":null}}");

        StravaToken token = response.getStravaToken();
        assertEquals("Bearer", token.getTokenType());
        assertEquals(1710125802, token.getExpiresAt());
        assertEquals(18621, token.getExpiresIn());
        assertEquals("4c528f5fe...", token.getRefreshToken());
        assertEquals("8e124b843...", token.getAccessToken());

        StravaAthlete athlete = response.getAthlete();
        assertEquals(1234567, athlete.getId());
        assertEquals("username", athlete.getUsername());
        assertEquals("firstName", athlete.getFirstname());
        assertEquals("lastName", athlete.getLastname());
        assertEquals("City", athlete.getCity());
        assertEquals("Country", athlete.getCountry());
    }

    @Test
    public void shouldParseBody_WithoutAthleteData() {
        StravaTokenResponse response = new StravaTokenResponse("{\"token_type\":\"Bearer\",\"access_token\":\"a9b723...\",\"expires_at\":1568775134,\"expires_in\":20566,\"refresh_token\":\"b5c569...\"}");
        StravaToken token = response.getStravaToken();
        assertEquals("Bearer", token.getTokenType());
        assertEquals(1568775134, token.getExpiresAt());
        assertEquals(20566, token.getExpiresIn());
        assertEquals("b5c569...", token.getRefreshToken());
        assertEquals("a9b723...", token.getAccessToken());
    }
}