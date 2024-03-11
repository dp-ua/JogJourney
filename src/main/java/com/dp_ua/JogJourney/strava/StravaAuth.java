package com.dp_ua.JogJourney.strava;

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
public class StravaAuth {
    public static final String STRAVA_API_URL = "https://www.strava.com/api/v3/";
    private static final String STRAVA_OAUTH_TOKEN_URL = STRAVA_API_URL + "oauth/token";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public SimpleEntry<StravaToken, StravaAthlete> tokenExchange(String clientId, String clientSecret, String code) {
        Map<Object, Object> data = prepareTokenExchangeData(clientId, clientSecret, code);

        HttpResponse<String> response = makeRequest(data, STRAVA_OAUTH_TOKEN_URL);

        if (response.statusCode() != 200) {
            log.error("Error during token exchange. Response code: " + response.statusCode());
            throw new RuntimeException("Error during token exchange. Response code: " + response.statusCode());
        }
        log.debug("tokenExchange: " + response.body());
        StravaTokenResponse stravaTokenResponse = new StravaTokenResponse(response.body());
        StravaToken token = stravaTokenResponse.getStravaToken();
        StravaAthlete athlete = stravaTokenResponse.getAthlete();
        return new SimpleEntry<>(token, athlete);
    }

    public StravaToken tokenRefresh(String clientId, String clientSecret, String refreshToken) {
        Map<Object, Object> data = prepareTokenRefreshData(clientId, clientSecret, refreshToken);

        HttpResponse<String> response = makeRequest(data, STRAVA_OAUTH_TOKEN_URL);

        if (response.statusCode() != 200) {
            log.error("Error during token refresh. Response code: " + response.statusCode());
            throw new RuntimeException("Error during token refresh. Response code: " + response.statusCode());
        }
        StravaToken token = new StravaTokenResponse(response.body()).getStravaToken();
        log.debug("tokenRefresh: " + token);

        return token;
    }

    private HttpResponse<String> makeRequest(Map<Object, Object> data, String url) {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(buildFormDataFromMap(data))
                    .uri(URI.create(url))
                    .header("Content-Type", APPLICATION_X_WWW_FORM_URLENCODED)
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // todo throw exception if response code is not 200
            //  401 - Unauthorized
            //  403 - Forbidden
            //  404 - Not Found
            //  429 - Too Many Requests
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