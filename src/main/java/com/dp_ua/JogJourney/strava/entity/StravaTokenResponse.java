package com.dp_ua.JogJourney.strava.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Setter
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

    public StravaTokenResponse(String body) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StravaTokenResponse response;
        try {
            response = mapper.readValue(body, StravaTokenResponse.class);
            this.tokenType = response.tokenType;
            this.expiresAt = response.expiresAt;
            this.expiresIn = response.expiresIn;
            this.refreshToken = response.refreshToken;
            this.accessToken = response.accessToken;
            this.athlete = response.athlete;
        } catch (Exception e) {
            log.error("Error during parsing token response", e);
        }
    }

    public StravaToken getStravaToken() {
        StravaToken token = new StravaToken();
        token.setTokenType(tokenType);
        token.setExpiresAt(expiresAt);
        token.setExpiresIn(expiresIn);
        token.setRefreshToken(refreshToken);
        token.setAccessToken(accessToken);
        return token;
    }
}
