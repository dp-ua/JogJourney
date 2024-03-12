package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;

import java.util.AbstractMap;

public interface StravaApiFacade {
    AbstractMap.SimpleEntry<StravaToken, StravaAthlete> exchangeCodeForToken(String code);

    StravaToken refreshToken(String refreshToken);

    String getStravaAuthUrl(String chatId);
}
