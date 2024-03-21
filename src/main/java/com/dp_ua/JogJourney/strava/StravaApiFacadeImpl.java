package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.exception.StravaApiException;
import com.dp_ua.JogJourney.dba.element.StravaActivity;
import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import com.dp_ua.JogJourney.dba.element.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StravaApiFacadeImpl implements StravaApiFacade {
    public static final String FIRST_DAY_OF_YEAR = "01-01T00:00:00Z";
    public static final String LAST_DAY_OF_YEAR = "12-31T23:59:59Z";
    private final String clientId;
    private final String clientSecret;
    public static final String STRAVA_URL = "https://www.strava.com/oauth";
    private final static String STRAVA_AUTH_URL = STRAVA_URL + "/authorize?client_id={YOUR_CLIENT_ID}&redirect_uri={YOUR_REDIRECT_URI}&response_type=code&scope=read,profile:read_all,activity:read_all";
    private final String redirectionUrl;
    public static final int PER_PAGE_SHOT_UPDATE = 30;
    public static final int PER_PAGE_LONG_UPDATE = 500;
    public static final int MAX_REQUEST_COUNT = 5;
    @Autowired
    StravaApi stravaApi;
    @Autowired
    StravaParser stravaParser;

    public StravaApiFacadeImpl(String clientId, String clientSecret, String redirectionUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectionUrl = redirectionUrl;
    }

    @Override
    public List<StravaActivity> loadAthleteActivities_ShortUpdate(StravaToken token, long before, long after) throws StravaApiException {
        List<StravaActivity> activities = loadAthleteActivities(token, before, after, PER_PAGE_SHOT_UPDATE);
        log.info("Short Update. Athlete: " + token.getStravaId() + " activities loaded: " + activities);
        return activities;
    }

    @Override
    public List<StravaActivity> loadAthleteActivities_LongUpdate(StravaToken token, int year) throws StravaApiException {
        validateYear(year);

        long after = getAfterFromYear(year);
        long before = getBeforeFromYear(year);

        List<StravaActivity> activities = loadAthleteActivities(token, before, after, PER_PAGE_LONG_UPDATE);
        log.info("Long Update. Athlete: " + token.getStravaId() + " activities loaded: " + activities);
        return activities;
    }

    private void validateTime(long before, long after) {
        if (before < after) {
            throw new IllegalArgumentException("Before should be after after. Before: " + before + " after: " + after);
        }
    }

    private long getBeforeFromYear(int year) {
        return getEpochSecondsForDay(year, LAST_DAY_OF_YEAR);
    }

    private long getAfterFromYear(int year) {
        return getEpochSecondsForDay(year, FIRST_DAY_OF_YEAR);
    }

    private static long getEpochSecondsForDay(int year, String day) {
        return Instant.parse(year + "-" + day).getEpochSecond();
    }

    private void validateYear(int year) {
        if (year < 2007 || year > ZonedDateTime.now().getYear()) {
            throw new IllegalArgumentException("Year should be between 2007 and current year");
        }
    }

    private List<StravaActivity> loadAthleteActivities(StravaToken token, long before, long after, int perPage) throws StravaApiException {
        validateTime(before, after);
        List<StravaActivity> result = new ArrayList<>();

        boolean breakFlag = false;
        int requestCount = 0;
        while (!breakFlag) {
            List<StravaActivity> activities = loadAthleteActivities(token, before, after, perPage, requestCount + 1);
            result.addAll(activities);
            if (activities.size() < perPage) {
                breakFlag = true;
            }
            requestCount++;
            if (requestCount > MAX_REQUEST_COUNT) {
                breakFlag = true;
            }
        }
        log.debug("Athlete: " + token.getStravaId() + "summary athlete activities loaded: " + result);
        return result;
    }

    private List<StravaActivity> loadAthleteActivities(StravaToken token, long before, long after, int perPage, int page) throws StravaApiException {
        log.debug("Going to load athlete activities for athlete: " + token.getStravaId() + " before: " + before + " after: " + after + " perPage: " + perPage + " page: " + page);

        String athleteActivitiesResponse = stravaApi.getAthleteActivities(before, after, page, perPage, token.getAccessToken());
        List<StravaActivity> activities = stravaParser.parseActivitiesResponse(athleteActivitiesResponse)
                .stream().map(StravaActivity::new).toList();
        log.debug("Athlete: " + token.getStravaId() + " athlete activities loaded: " + activities);

        return activities;
    }

    @Override
    public SimpleEntry<StravaToken, StravaAthlete> exchangeCodeForToken(String code) {
        log.info("Get token for code: " + code);
        return stravaApi.tokenExchange(clientId, clientSecret, code);
    }

    @Override
    public StravaToken refreshToken(String refreshToken) throws StravaApiException {
        log.info("Going to refresh with refreshToken: " + refreshToken);
        return stravaApi.tokenRefresh(clientId, clientSecret, refreshToken);
    }

    @Override
    public String getStravaAuthUrl(String chatId) {
        return STRAVA_AUTH_URL.replace("{YOUR_CLIENT_ID}", clientId).replace("{YOUR_REDIRECT_URI}", redirectionUrl.replace("{chatId}", chatId));
    }
}
