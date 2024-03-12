package com.dp_ua.JogJourney.strava;

import com.dp_ua.JogJourney.bot.event.SendMessageEvent;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.dp_ua.JogJourney.service.MessageCreator.END_LINE;
import static com.dp_ua.JogJourney.service.MessageCreator.SERVICE;

@Component
public class StravaMessage {
    public SendMessageEvent getGoToStravaAuthPageMessage(String chatId, String url) {
        InlineKeyboardMarkup keyboard = getAuthButtonMarkup(url);
        return SERVICE.getSendMessageEvent(chatId, getAuthMessage(), keyboard, null);
    }

    private String getAuthMessage() {
        return "Для початку вам потрібно надати мені дозвіл дивитись ваш профіль в Strava." +
                END_LINE +
                END_LINE +
                " Для цього натисніть на кнопку нижче.";
    }

    private String getAuthButtonText() {
        return "Авторизувати бота в Strava";
    }


    private InlineKeyboardMarkup getAuthButtonMarkup(String url) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton urlButton = new InlineKeyboardButton();
        urlButton.setText(getAuthButtonText());
        urlButton.setUrl(url);
        row.add(urlButton);

        rows.add(row);
        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public SendMessageEvent getAlreadyAuthMessage(String chatId) {
        return SERVICE.getSendMessageEvent(chatId, "Бот вже авторизований в Strava\n\n Token дійсний", null, null);
    }

    public SendMessageEvent getRefreshTokenMessage(String chatId) {
        return SERVICE.getSendMessageEvent(chatId, "Бот вже авторизований в Strava\n\n Token оновлено", null, null);
    }
}
