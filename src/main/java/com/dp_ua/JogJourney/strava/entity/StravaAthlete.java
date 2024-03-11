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
    private String firstname;
    private String lastname;
    private String sex;
    private String city;
    private String country;
    private String profile;
    private String created_at;
    private String updated_at;
}
