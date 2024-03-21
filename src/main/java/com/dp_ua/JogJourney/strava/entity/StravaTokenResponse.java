package com.dp_ua.JogJourney.strava.entity;

import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Setter
@Getter
public class StravaTokenResponse {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_at")
    private long expiresAt;
    @JsonProperty("expires_in")
    private long expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("access_token")
    private String accessToken;
    @Getter
    private StravaAthlete athlete;
}
