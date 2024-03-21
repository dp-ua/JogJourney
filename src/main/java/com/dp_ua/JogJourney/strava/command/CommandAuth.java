package com.dp_ua.JogJourney.strava.command;

import com.dp_ua.JogJourney.bot.command.CommandInterface;
import com.dp_ua.JogJourney.bot.message.Message;
import com.dp_ua.JogJourney.strava.StravaFacade;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ToString
@EqualsAndHashCode
public class CommandAuth implements CommandInterface {
    public static final String command = "auth";
    private final boolean isInTextCommand = false;
    @Autowired
    StravaFacade stravaFacade;

    @Override
    public String command() {
        return command;
    }

    @Override
    public void execute(Message message) {
        String chatId = message.getChatId();
//        stravaFacade.loadAthleteActivities(chatId);
    }
}