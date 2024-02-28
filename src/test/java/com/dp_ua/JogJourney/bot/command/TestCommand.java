package com.dp_ua.JogJourney.bot.command;

import com.dp_ua.JogJourney.bot.message.Message;

public class TestCommand implements CommandInterface {
    @Override
    public String command() {
        return "test";
    }

    @Override
    public void execute(Message message) {
        // do nothing
    }
}
