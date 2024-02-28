package com.dp_ua.JogJourney.bot.command;

import com.dp_ua.JogJourney.exeption.NotForMeException;

import java.util.List;

public interface TextCommandDetectorService {
    List<CommandInterface> getParsedCommands(String text) throws NotForMeException;
}
