package com.dp_ua.JogJourney.strava.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class StravaToken {
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

    @Override
    public String toString() {
        return "TokenResponse{" +
                "tokenType='" + tokenType + '\'' +
                ", expiresAt=" + expiresAt +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
