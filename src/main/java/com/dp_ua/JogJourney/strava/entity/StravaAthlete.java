package com.dp_ua.JogJourney.strava.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Slf4j
public class StravaAthlete {
    private long id;
    private String username;
    private String resourceState;
    private String firstname;
    private String lastname;
    private String city;
    private String country;
}
