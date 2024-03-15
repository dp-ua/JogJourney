package com.dp_ua.JogJourney;

import com.dp_ua.JogJourney.bot.Bot;
import com.dp_ua.JogJourney.strava.StravaApiFacade;
import com.dp_ua.JogJourney.strava.StravaApiFacadeImpl;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
@EnableAsync
@EnableScheduling
public class AppConfig {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.name}")
    private String botUserName;

    @Value("${strava.client.id}")
    private String clientId;
    @Value("${strava.client.secret}")
    private String clientSecret;

    @Value("${strava.redirectionUrl}")
    private String redirectionUrl;


    @Bean
    public Bot bot() {
        log.info("Bot name:[" + botUserName + "] Bot token:[" + botToken.substring(0, 10) + "...]");
        return new Bot(botToken, botUserName);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public StravaApiFacade stravaOperator() {
        log.debug("Create StravaApiFacadeImpl with clientId: " + clientId + " clientSecret: " + clientSecret + " redirectionUrl: " + redirectionUrl);
        return new StravaApiFacadeImpl(clientId, clientSecret, redirectionUrl);
    }
}
