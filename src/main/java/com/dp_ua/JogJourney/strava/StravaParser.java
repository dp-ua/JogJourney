package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.strava.entity.StravaActivityResponse;
import com.dp_ua.JogJourney.strava.entity.StravaTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Component
public class StravaParser {
    public StravaTokenResponse parseTokenResponse(String body) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(body, StravaTokenResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Error[%s] during parsing token response[%s]", e.getMessage(), body));
        }
    }

    public List<StravaActivityResponse> parseActivitiesResponse(String body) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(body, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Error[%s] during parsing activities response[%s]", e.getMessage(), body));
        }
    }
}
