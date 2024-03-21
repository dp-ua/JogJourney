package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.exception.StravaApiException;
import com.dp_ua.JogJourney.dba.element.StravaActivity;
import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import com.dp_ua.JogJourney.dba.element.StravaToken;

import java.util.AbstractMap;
import java.util.List;

public interface StravaApiFacade {

    AbstractMap.SimpleEntry<StravaToken, StravaAthlete> exchangeCodeForToken(String code);

    StravaToken refreshToken(String refreshToken) throws StravaApiException;

    String getStravaAuthUrl(String chatId);

    List<StravaActivity> loadAthleteActivities_ShortUpdate(StravaToken token, long before, long after) throws StravaApiException;

    List<StravaActivity> loadAthleteActivities_LongUpdate(StravaToken token, int year) throws StravaApiException;
}
