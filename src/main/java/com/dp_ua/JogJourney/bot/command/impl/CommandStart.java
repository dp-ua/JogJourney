package com.dp_ua.JogJourney.bot.command.impl;

import com.dp_ua.JogJourney.bot.Bot;
import com.dp_ua.JogJourney.bot.command.CommandInterface;
import com.dp_ua.JogJourney.bot.event.SendMessageEvent;
import com.dp_ua.JogJourney.bot.message.Message;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static com.dp_ua.JogJourney.service.MessageCreator.SERVICE;

@Component
@ToString
@EqualsAndHashCode
public class CommandStart implements CommandInterface {
    public static final String command = "start";
    private final boolean isInTextCommand = false;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public String command() {
        return command;
    }

    @Override
    public void execute(Message message) {
        StringBuilder sb = getStartMessageText();
        sendMessage(message, sb);
    }

    private StringBuilder getStartMessageText() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Привет. Я бот для поиска компаний в гугле");
        return sb;
    }

    private void sendMessage(Message message, StringBuilder sb) {
        String chatId = message.getChatId();
        SendMessageEvent sendMessageEvent = SERVICE.getSendMessageEvent(chatId, sb.toString(), null, message.getEditMessageId());
        publisher.publishEvent(sendMessageEvent);
    }
}
