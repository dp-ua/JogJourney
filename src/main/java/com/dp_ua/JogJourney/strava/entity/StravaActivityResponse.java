package com.dp_ua.JogJourney.strava.entity;

import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
@Setter
@Getter
public class StravaActivityResponse {
    @JsonProperty("athlete")
    private StravaAthlete athlete;
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("distance")
    private double distance;
    @JsonProperty("moving_time")
    private int movingTime;
    @JsonProperty("elapsed_time")
    private int elapsedTime;
    @JsonProperty("type")
    private String type;
    @JsonProperty("sport_type")
    private String sportType;
    @JsonProperty("start_date_local")
    private String startDate;
}
