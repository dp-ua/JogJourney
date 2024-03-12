package com.dp_ua.JogJourney.strava;


import com.dp_ua.JogJourney.bot.event.SendMessageEvent;
import com.dp_ua.JogJourney.dba.service.StravaAthleteService;
import com.dp_ua.JogJourney.dba.service.StravaTokenService;
import com.dp_ua.JogJourney.strava.entity.StravaAthlete;
import com.dp_ua.JogJourney.strava.entity.StravaToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

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
        Optional<StravaAthlete> athlete = stravaAthleteService.findByChatId(chatId);
        if (athlete.isPresent()) {
            log.debug("For chatId: " + chatId + " athlete is present: " + athlete.get());
            Optional<StravaToken> token = stravaTokenService.findByStravaId(athlete.get().getStravaId());
            if (token.isPresent()) {
                log.debug("For chatId: " + chatId + " user already auth");
                // just for check if user already auth - refresh token  todo remove this
                StravaToken oldToken = token.get();
                if (oldToken.isExpired()) {
                    log.debug("For chatId: " + chatId + " oldToken: " + oldToken);
                    StravaToken newToken = stravaApiFacade.refreshToken(oldToken.getRefreshToken());
                    stravaTokenService.saveOrUpdate(newToken);
                    log.debug("For chatId: " + chatId + " newToken: " + newToken);
                    SendMessageEvent event = stravaMessage.getRefreshTokenMessage(chatId);
                    publisher.publishEvent(event);
                    return;
                } else {
                    log.debug("Not expired token: For chatId: " + chatId + " oldToken: " + oldToken);
                    SendMessageEvent event = stravaMessage.getAlreadyAuthMessage(chatId);
                    publisher.publishEvent(event);
                    return;
                }
            }
        }
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
}