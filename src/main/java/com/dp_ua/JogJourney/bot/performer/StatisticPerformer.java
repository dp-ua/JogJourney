package com.dp_ua.JogJourney.bot.performer;

import com.dp_ua.JogJourney.bot.event.GetMessageEvent;
import com.dp_ua.JogJourney.bot.message.Message;
import com.dp_ua.JogJourney.dba.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.dp_ua.JogJourney.bot.message.Message.Type.SELF;

@Component
@Slf4j
public class StatisticPerformer implements ApplicationListener<GetMessageEvent> {
    @Autowired
    StatisticService statisticService;

    @Override
    public void onApplicationEvent(GetMessageEvent event) {
        Message message = event.getMessage();
        log.debug("StatisticPerformer: {}", message);

        if (message.getType() != SELF) {
            statisticService.save(message.getChatId(), message.getUserName(), message.getMessageText());
        }
    }
}
