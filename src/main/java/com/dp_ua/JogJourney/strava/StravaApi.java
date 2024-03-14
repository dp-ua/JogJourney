package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.exception.StravaApiException;
import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import com.dp_ua.JogJourney.strava.entity.StravaTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class StravaApi {
    public static final String STRAVA_API_URL = "https://www.strava.com/api/v3/";
    private static final String STRAVA_OAUTH_TOKEN_URL = STRAVA_API_URL + "oauth/token";
    private static final String STRAVA_ATHLETE_ACTIVITIES_URL = STRAVA_API_URL + "athlete/activities";
        public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public String getAthleteActivities(long before, long after, int page, int perPage, String token) throws StravaApiException {
        Map<Object, Object> data = prepareDataForActivitiesRequest(before, after, page, perPage);
        HttpResponse<String> response = makeGetRequest(data, token, STRAVA_ATHLETE_ACTIVITIES_URL);
        log.debug("getAthleteActivities: " + response.body());
        return response.body();
    }

    private HttpResponse<String> makeGetRequest(Map<Object, Object> data, String token, String url) throws StravaApiException {
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "?" + buildFormDataFromMap(data)))
                .header("Authorization", "Bearer " + token)
                .header("accept", "application/json")
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.error("Error during sending request. Response code: " + response.statusCode());
                throw new StravaApiException("Error during sending request", response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error during sending request", e);
            throw new RuntimeException(e);
        }
        return response;
    }

    private Map<Object, Object> prepareDataForActivitiesRequest(long before, long after, int page, int perPage) {
        Map<Object, Object> data = new HashMap<>();
        data.put("before", before);
        data.put("after", after);
        data.put("page", page);
        data.put("per_page", perPage);
        return data;
    }

    public SimpleEntry<StravaToken, StravaAthlete> tokenExchange(String clientId, String clientSecret, String code) {
        Map<Object, Object> data = prepareTokenExchangeData(clientId, clientSecret, code);

        try {
            HttpResponse<String> response = makePostRequestWithoutAuthorization(data, STRAVA_OAUTH_TOKEN_URL);
            log.debug("tokenExchange: " + response.body());
            StravaTokenResponse stravaTokenResponse = new StravaTokenResponse(response.body());
            StravaToken token = stravaTokenResponse.getStravaToken();
            StravaAthlete athlete = stravaTokenResponse.getAthlete();
            return new SimpleEntry<>(token, athlete);
        } catch (StravaApiException e) {
            log.error("Error during token exchange. Response code: " + e.getCode());
            // todo пока не знаю какие проблемы могут возникнуть при обмене кода на токен
            throw new RuntimeException("Error during token exchange. Response code: " + e.getCode());
        }
    }

    public StravaToken tokenRefresh(String clientId, String clientSecret, String refreshToken) throws StravaApiException {
        Map<Object, Object> data = prepareTokenRefreshData(clientId, clientSecret, refreshToken);

        HttpResponse<String> response = makePostRequestWithoutAuthorization(data, STRAVA_OAUTH_TOKEN_URL);
        StravaToken token = new StravaTokenResponse(response.body()).getStravaToken();
        log.debug("tokenRefresh: " + token);

        return token;
    }


    private HttpResponse<String> makePostRequestWithoutAuthorization(Map<Object, Object> data, String url) throws StravaApiException {
        return makePostRequest(data, url, null);
    }

    private HttpResponse<String> makePostRequest(Map<Object, Object> data, String url, String token) throws StravaApiException {
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        // add token if it is not null
        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create(url))
                .header("Content-Type", APPLICATION_X_WWW_FORM_URLENCODED)
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.error("Error during sending request. Response code: " + response.statusCode());
                throw new StravaApiException("Error during sending request", response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error during sending request", e);
            throw new RuntimeException(e);
        }
        return response;
    }

    private Map<Object, Object> prepareTokenExchangeData(String clientId, String clientSecret, String code) {
        Map<Object, Object> data = new HashMap<>();
        data.put("client_id", clientId);
        data.put("client_secret", clientSecret);
        data.put("code", code);
        data.put("grant_type", "authorization_code");
        return data;
    }

    private Map<Object, Object> prepareTokenRefreshData(String clientId, String clientSecret, String refreshToken) {
        Map<Object, Object> data = new HashMap<>();
        data.put("client_id", clientId);
        data.put("client_secret", clientSecret);
        data.put("refresh_token", refreshToken);
        data.put("grant_type", "refresh_token");
        return data;
    }

    private HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();

        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (!builder.isEmpty()) {
                builder.append("&");
            }
            builder.append(encode(entry.getKey().toString(), UTF_8));
            builder.append("=");
            builder.append(encode(entry.getValue().toString(), UTF_8));
        }

        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}