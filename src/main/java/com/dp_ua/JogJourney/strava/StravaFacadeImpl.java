package com.dp_ua.JogJourney.strava;


import com.dp_ua.JogJourney.bot.event.SendMessageEvent;
import com.dp_ua.JogJourney.dba.service.StravaActivityService;
import com.dp_ua.JogJourney.dba.service.StravaAthleteService;
import com.dp_ua.JogJourney.dba.service.StravaTokenService;
import com.dp_ua.JogJourney.dba.service.SubscribeService;
import com.dp_ua.JogJourney.exception.StravaApiException;
import com.dp_ua.JogJourney.dba.element.StravaActivity;
import com.dp_ua.JogJourney.dba.element.StravaAthlete;
import com.dp_ua.JogJourney.dba.element.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class StravaFacadeImpl implements StravaFacade {
    @Autowired
    StravaMessage stravaMessage;
    @Autowired
    StravaApiFacade stravaApiFacade;
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    StravaAthleteService stravaAthleteService;
    @Autowired
    StravaTokenService stravaTokenService;
    @Autowired
    StravaActivityService stravaActivityService;
    @Autowired
    SubscribeService subscribeService;


    @Override
    public void operateStravaAuth(String chatId) {
        String stravaAuthUrl = stravaApiFacade.getStravaAuthUrl(chatId);
        log.info("For chatId: " + chatId + " send message with stravaAuthUrl: " + stravaAuthUrl);
        SendMessageEvent event = stravaMessage.getGoToStravaAuthPageMessage(chatId, stravaAuthUrl);
        publisher.publishEvent(event);
    }

    @Override
    public StravaAthlete operateStravaCode(String code, String chatId, String scope) {
        log.info("For chatId: " + chatId + " operateStravaCode: " + code + " scope: " + scope);
        SimpleEntry<StravaToken, StravaAthlete> stravaTokenAndAthlete = stravaApiFacade.exchangeCodeForToken(code);

        StravaToken token = stravaTokenAndAthlete.getKey();
        StravaAthlete athlete = stravaTokenAndAthlete.getValue();

        token.setStravaId(athlete.getStravaId());
        athlete.setChatId(chatId);

        log.debug("For chatId: " + chatId + " token: " + stravaTokenAndAthlete.getKey());
        log.debug("For chatId: " + chatId + " athlete: " + stravaTokenAndAthlete.getValue());

        stravaTokenService.saveOrUpdate(token);
        // todo send message to user
        return stravaAthleteService.save(athlete);
    }

    @Override
    public void loadAthleteActivities(String chatId, long before, long after) {
        try {
            StravaAthlete athlete = getAthleteForChat(chatId);
            checkAthlete(chatId, athlete);

            StravaToken token = getStravaTokenForAthlete(athlete);
            checkToken(token, athlete);

            if (token.isExpired()) {
                token = refreshToken(token);
            }

            List<StravaActivity> activities;
            if (after == 0) {
                activities = stravaApiFacade.loadAthleteActivities_LongUpdate(token, getYear());
            } else {
                activities = stravaApiFacade.loadAthleteActivities_ShortUpdate(token, before, after);
            }
            log.info("For chatId: " + chatId + "Athlete activities loaded: " + activities.size());
            activities.forEach(activity -> stravaActivityService.saveOrUpdate(activity));

        } catch (StravaApiException e) {
            log.error("Error during loading athlete activities", e);
            operateStravaApiException(chatId, e);
        } catch (AccountNotFoundException e) {
            log.error("Error during loading athlete activities. {}", e.getMessage());
            operateAccountNotFoundException(chatId);
        }
    }

    private static int getYear() {
        return Instant.now().atZone(ZoneId.systemDefault()).getYear();
    }

    private void operateAccountNotFoundException(String chatId) {
        operateStravaAuth(chatId);
    }

    private void operateStravaApiException(String chatId, StravaApiException e) {
        operateStravaAuth(chatId);
        log.error("StravaApiException, Not Implemented yet", e);
    }

    private StravaToken refreshToken(StravaToken token) throws StravaApiException {
        return stravaApiFacade.refreshToken(token.getRefreshToken());
    }

    private void checkToken(StravaToken token, StravaAthlete athlete) throws AccountNotFoundException {
        if (token == null) {
            throw new AccountNotFoundException("No token for athlete: " + athlete);
        }
    }

    private void checkAthlete(String chatId, StravaAthlete athlete) throws AccountNotFoundException {
        if (athlete == null) {
            throw new AccountNotFoundException("No athlete for chatId: " + chatId);
        }
    }

    private StravaToken getStravaTokenForAthlete(StravaAthlete athlete) {
        Optional<StravaToken> token = stravaTokenService.findByStravaId(athlete.getStravaId());
        return token.orElse(null);
    }

    private StravaAthlete getAthleteForChat(String chatId) {
        Optional<StravaAthlete> athlete = stravaAthleteService.findByChatId(chatId);
        return athlete.orElse(null);
    }
}