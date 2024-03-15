package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.exception.StravaApiException;
import com.dp_ua.JogJourney.strava.entity.StravaActivity;
import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;

import java.util.AbstractMap;
import java.util.List;

public interface StravaApiFacade {
    AbstractMap.SimpleEntry<StravaToken, StravaAthlete> exchangeCodeForToken(String code);

    StravaToken refreshToken(String refreshToken) throws StravaApiException;

    String getStravaAuthUrl(String chatId);

    List<StravaActivity> loadAthleteActivities(StravaToken token, long before, long after) throws StravaApiException;
}
