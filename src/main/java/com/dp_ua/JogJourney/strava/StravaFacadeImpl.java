package com.dp_ua.JogJourney.strava;


import com.dp_ua.JogJourney.bot.event.SendMessageEvent;
import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.AbstractMap.SimpleEntry;

@Slf4j
public class StravaFacadeImpl implements StravaFacade {
    @Autowired
    StravaMessage stravaMessage;
    @Autowired
    StravaApi stravaOperator;
    @Autowired
    ApplicationEventPublisher publisher;

    @Override
    public void operateStravaAuth(String chatId) {
        if (isUserAlreadyAuth(chatId)) {
            log.info("For chatId: " + chatId + " user already auth");
            return;
        }
        String stravaAuthUrl = stravaOperator.getStravaAuthUrl(chatId);
        log.info("For chatId: " + chatId + " send message with stravaAuthUrl: " + stravaAuthUrl);
        SendMessageEvent event = stravaMessage.getGoToStravaAuthPageMessage(chatId, stravaAuthUrl);
        publisher.publishEvent(event);
    }

    private boolean isUserAlreadyAuth(String chatId) {
        //TODO check in DB
        log.warn("For chatId: " + chatId + " isUserAlreadyAuth not implemented");
        return false;
    }

    @Override
    public StravaAthlete operateStravaCode(String code, String chatId, String scope) {
        log.info("For chatId: " + chatId + " operateStravaCode: " + code + " scope: " + scope);
        SimpleEntry<StravaToken, StravaAthlete> stravaTokenAndAthlete = stravaOperator.exchangeCodeForToken(code);
        log.info("For chatId: " + chatId + " token: " + stravaTokenAndAthlete.getKey());
        log.info("For chatId: " + chatId + " athlete: " + stravaTokenAndAthlete.getValue());
        return stravaTokenAndAthlete.getValue();
    }
}