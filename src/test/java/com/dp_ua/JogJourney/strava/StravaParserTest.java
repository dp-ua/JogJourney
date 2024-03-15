package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.strava.entity.StravaActivityResponse;
import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import com.dp_ua.JogJourney.strava.entity.StravaTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class StravaParserTest {
    private StravaParser parser;
    private static final String TOKEN_RESPONSE_WITH_ATHLETE = "token_response_withAthlete.json";
    private static final String TOKEN_RESPONSE_WITHOUT_ATHLETE = "token_response_withoutAthlete.json";
    private static final String ACTIVITIES_RESPONSE_BODY = "activities_response.json";

    @BeforeEach
    void setUp() {
        parser = new StravaParser();
    }

    @Test
    void shouldParseToken_WithAthlete() {
        String responseBody = getFile(TOKEN_RESPONSE_WITH_ATHLETE);
        StravaTokenResponse response = parser.parseTokenResponse(responseBody);

        StravaToken token = new StravaToken(response);

        assertEquals("Bearer", token.getTokenType());
        assertEquals(1710386376, token.getExpiresAt());
        assertEquals(19443, token.getExpiresIn());
        assertEquals("4c528f5fef...", token.getRefreshToken());
        assertEquals("bc9b04b6d9....", token.getAccessToken());

        StravaAthlete athlete = response.getAthlete();
        assertEquals(1234567, athlete.getStravaId());
        assertEquals("userName", athlete.getUsername());
        assertEquals("firstName", athlete.getFirstname());
        assertEquals("lastName", athlete.getLastname());
        assertEquals("City", athlete.getCity());
        assertEquals("Country", athlete.getCountry());
        assertEquals("M", athlete.getSex());
        assertEquals("2020-07-20T09:25:54Z", athlete.getStravaCreatedAt());
        assertEquals("2023-10-21T08:04:52Z", athlete.getStravaUpdatedAt());
        assertEquals("https://large.jpg", athlete.getProfile());
    }

    @Test
    public void shouldParseToken_WithoutAthleteData() {
        String responseBody = getFile(TOKEN_RESPONSE_WITHOUT_ATHLETE);
        StravaTokenResponse response = parser.parseTokenResponse(responseBody);

        StravaToken token = new StravaToken(response);

        assertEquals("Bearer", token.getTokenType());
        assertEquals(1710386376, token.getExpiresAt());
        assertEquals(19443, token.getExpiresIn());
        assertEquals("4c528f5fef...", token.getRefreshToken());
        assertEquals("bc9b04b6d9....", token.getAccessToken());
    }

    @Test
    void shouldParseListOfStravaActivities() {
        String responseBody = getFile(ACTIVITIES_RESPONSE_BODY);
        List<StravaActivityResponse> response = parser.parseActivitiesResponse(responseBody);

        assertEquals(3, response.size());
        for (int i = 0; i < 3; i++) {
            StravaActivityResponse activity = response.get(i);
            assertEquals((i + 1), activity.getId());
            assertEquals("activity_" + (i + 1), activity.getName(), "activity_name. i=" + i);
            assertEquals(i + 1, activity.getDistance(), "distance i=" + i);
            assertEquals(i + 1, activity.getMovingTime(), "moving_time i=" + i);
            assertEquals(i + 1, activity.getElapsedTime(), "elapsed_time i=" + i);
            assertEquals("Run", activity.getType(), "type i=" + i);
            assertEquals("Run", activity.getSportType(), "sport_type i=" + i);
            assertEquals("2024-03-13T07:35:55Z", activity.getStartDate(), "start_date_local i=" + i);
        }
    }

    private String getFile(String fileName) {
        log.info("parseTokenResponse from file: {}", fileName);
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(fileName).toURI())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        log.info("File content: {}", fileContent.replaceAll("\n", ""));
        return fileContent;
    }
}