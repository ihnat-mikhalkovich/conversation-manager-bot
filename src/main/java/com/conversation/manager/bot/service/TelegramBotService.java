package com.conversation.manager.bot.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Service
@Getter
@Setter
@Slf4j
public class TelegramBotService extends TelegramWebhookBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.path}")
    private String botPath;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final Message message = update.getMessage();
        if (Objects.isNull(message)) {
            return null;
        }

        if (message.hasText()) {
            final String text = update.getMessage().getText();
            log.info("I got the message: {}", text);
            final Long chatId = message.getChatId();
            return new SendMessage(chatId, "Hi " + text);
        }

        return null;
    }
}
