package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.exception.StravaApiException;
import com.dp_ua.JogJourney.strava.entity.StravaActivity;
import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

@Slf4j
public class StravaApiFacadeImpl implements StravaApiFacade {
    private final String clientId;
    private final String clientSecret;
    public static final String STRAVA_URL = "https://www.strava.com/oauth";
    private final static String STRAVA_AUTH_URL = STRAVA_URL + "/authorize?client_id={YOUR_CLIENT_ID}&redirect_uri={YOUR_REDIRECT_URI}&response_type=code&scope=read,profile:read_all,activity:read_all";
    private final String redirectionUrl;
    public static final int PER_PAGE = 5;
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
    public List<StravaActivity> loadAthleteActivities(StravaToken token, long before, long after) throws StravaApiException {
        log.info("Going to load athlete activities for athlete: " + token.getStravaId() + " before: " + before + " after: " + after);
        // todo add pagination if results more than STRAVA_API_PER_PAGE
        String athleteActivitiesResponse = stravaApi.getAthleteActivities(before, after, 1, PER_PAGE, token.getAccessToken());
        List<StravaActivity> activities = stravaParser.parseActivitiesResponse(athleteActivitiesResponse)
                .stream().map(StravaActivity::new).toList();
        log.info("Athlete activities loaded: " + activities.size());
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
