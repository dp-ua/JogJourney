package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.strava.entity.StravaAthlete;

public interface StravaFacade {
    void operateStravaAuth(String chatId);

    StravaAthlete operateStravaCode(String code, String chatId, String scope);

    void loadAthleteActivities(String chatId, long before, long after);

}