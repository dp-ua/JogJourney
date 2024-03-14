package com.dp_ua.JogJourney.strava;


import com.dp_ua.JogJourney.bot.event.SendMessageEvent;
import com.dp_ua.JogJourney.dba.service.StravaAthleteService;
import com.dp_ua.JogJourney.dba.service.StravaTokenService;
import com.dp_ua.JogJourney.exception.StravaApiException;
import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.util.AbstractMap.SimpleEntry;
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
            checkTime(before, after);

            StravaAthlete athlete = getAthleteForChat(chatId);
            checkAthlete(chatId, athlete);

            StravaToken token = getStravaTokenForAthlete(athlete);
            checkToken(token, athlete);

            if (token.isExpired()) {
                token = refreshToken(token);
            }

            String activities = stravaApiFacade.loadAthleteActivities(token, before, after);
            log.info("For chatId: " + chatId + " athlete activities: " + activities);
            // todo save activities
            // todo send message to user

        } catch (StravaApiException e) {
            log.error("Error during loading athlete activities", e);
            operateStravaApiException(chatId, e);
        } catch (AccountNotFoundException e) {
            log.error("Error during loading athlete activities", e);
            operateAccountNotFoundException(chatId, e);
        }
    }

    private void operateAccountNotFoundException(String chatId, AccountNotFoundException e) {
        operateStravaAuth(chatId);
        log.error("AccountNotFoundException, Not Implemented yet", e);
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
        return token.isPresent() ? token.get() : null;
    }

    private void checkTime(long before, long after) {
        if (before < after) {
            throw new IllegalArgumentException("before should be more than after");
        }
    }

    private StravaAthlete getAthleteForChat(String chatId) {
        Optional<StravaAthlete> athlete = stravaAthleteService.findByChatId(chatId);
        return athlete.isPresent() ? athlete.get() : null;
    }
}