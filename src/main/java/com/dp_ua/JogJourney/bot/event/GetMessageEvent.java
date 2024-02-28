package com.dp_ua.JogJourney.bot.event;

import com.dp_ua.JogJourney.bot.message.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GetMessageEvent extends ApplicationEvent {
    private final Message message;

    public GetMessageEvent(Object source, Message message) {
        super(source);
        this.message = message;

    }
}
