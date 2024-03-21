package com.dp_ua.JogJourney.dba.element;

import com.dp_ua.JogJourney.dba.element.DomainElement;
import com.dp_ua.JogJourney.strava.entity.StravaTokenResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Slf4j
public class StravaToken extends DomainElement {
    private long stravaId;
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

    public boolean isExpired() {
        Instant now = Instant.now();
        Instant expires = Instant.ofEpochMilli(expiresAt);
        return expires.isBefore(now);
    }

    public StravaToken(StravaTokenResponse response) {
        this.tokenType = response.getTokenType();
        this.expiresAt = response.getExpiresAt();
        this.expiresIn = response.getExpiresIn();
        this.refreshToken = response.getRefreshToken();
        this.accessToken = response.getAccessToken();
    }

    @Override
    public String toString() {
        return "StravaToken{" +
                "stravaId=" + stravaId +
                ", tokenType='" + tokenType + '\'' +
                ", expiresAt=" + expiresAt +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
