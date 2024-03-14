package com.dp_ua.JogJourney.strava.command;

import com.dp_ua.JogJourney.bot.command.CommandInterface;
import com.dp_ua.JogJourney.bot.message.Message;
import com.dp_ua.JogJourney.strava.StravaFacade;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

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
//        Instant now = Instant.now();
        // An epoch timestamp to use for filtering activities that have taken place before a certain time.
        //
        //before
        //after
        //integer
        //(query)
        //An epoch timestamp to use for filtering activities that have taken place after a certain time.
        // before and after - последние 48 часов с текущего момента
//        use OffsetDateTime.now().minusSeconds(48 * 60 * 60).toEpochSecond() and OffsetDateTime.now().toEpochSecond()
        long after = OffsetDateTime.now().minusSeconds(48 * 60 * 60).toEpochSecond();
        long before = OffsetDateTime.now().toEpochSecond();
        stravaFacade.loadAthleteActivities(chatId, before, after);
    }
}