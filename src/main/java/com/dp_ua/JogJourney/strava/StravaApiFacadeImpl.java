package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.AbstractMap.SimpleEntry;

@Slf4j
public class StravaApiFacadeImpl implements StravaApiFacade {
    private final String clientId;
    private final String clientSecret;
    public static final String STRAVA_URL = "https://www.strava.com/oauth";
    private final static String STRAVA_AUTH_URL = STRAVA_URL + "/authorize?client_id={YOUR_CLIENT_ID}&redirect_uri={YOUR_REDIRECT_URI}&response_type=code&scope=read,profile:read_all,activity:read_all";
    private final static String REDIRECTION_URL = "http://localhost:8080/strava/redirect?chatId={chatId}";
    @Autowired
    StravaApi stravaApi;

    public StravaApiFacadeImpl(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public SimpleEntry<StravaToken, StravaAthlete> exchangeCodeForToken(String code) {
        log.info("Get token for code: " + code);
        return stravaApi.tokenExchange(clientId, clientSecret, code);
    }

    @Override
    public StravaToken refreshToken(String refreshToken) {
        log.info("Going to refresh with refreshToken: " + refreshToken);
        return stravaApi.tokenRefresh(clientId, clientSecret, refreshToken);
    }

    @Override
    public String getStravaAuthUrl(String chatId) {
        return STRAVA_AUTH_URL.replace("{YOUR_CLIENT_ID}", clientId).replace("{YOUR_REDIRECT_URI}", REDIRECTION_URL.replace("{chatId}", chatId));
    }
}