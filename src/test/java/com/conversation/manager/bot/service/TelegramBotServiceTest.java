package com.conversation.manager.bot.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TelegramBotServiceTest {

    @Autowired
    private TelegramWebhookBot telegramWebhookBot;

    @Test
    public void fieldSetCorrectly() {
        assertEquals("the_bot_name", telegramWebhookBot.getBotUsername());
        assertEquals("the_bot_path", telegramWebhookBot.getBotPath());
        assertEquals("the_bot_token", telegramWebhookBot.getBotToken());
    }
}