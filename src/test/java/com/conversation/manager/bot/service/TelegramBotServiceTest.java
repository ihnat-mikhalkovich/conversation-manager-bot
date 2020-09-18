package com.conversation.manager.bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TelegramBotServiceTest {

    @Autowired
    private TelegramWebhookBot telegramWebhookBot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Test
    public void fieldSetCorrectly() {
        assertEquals("the_bot_name", telegramWebhookBot.getBotUsername());
        assertEquals("the_bot_path", telegramWebhookBot.getBotPath());
        assertEquals("the_bot_token", telegramWebhookBot.getBotToken());
    }

    @BeforeEach
    public void setUpUpdate() {
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.getChatId()).thenReturn(1L);
        when(message.hasText()).thenReturn(true);
    }

    @Test
    public void help() {
        when(message.getText()).thenReturn("/help");
        when(message.getChatId()).thenReturn(1L);
        final SendMessage sendMessage = (SendMessage) telegramWebhookBot.onWebhookUpdateReceived(update);
        final String text = sendMessage.getText();
        assertEquals("/help - info about commands", text);
        assertEquals("1", sendMessage.getChatId());
    }

    @Test
    public void start() {
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(2L);
        final SendMessage sendMessage = (SendMessage) telegramWebhookBot.onWebhookUpdateReceived(update);
        final String text = sendMessage.getText();
        assertEquals("Hi nice to meet you. I'm a conversation manager. You can be easily removed from from the group and invited again by me. To more get info, please, enter /help command.", text);
        assertEquals("2", sendMessage.getChatId());
    }

    @Test
    public void wrongCommand() {
        when(message.getText()).thenReturn("/abcdef");
        when(message.getChatId()).thenReturn(4L);
        final SendMessage sendMessage = (SendMessage) telegramWebhookBot.onWebhookUpdateReceived(update);
        final String text = sendMessage.getText();
        assertEquals("I can't recognize the command '/abcdef'. Could you try again?", text);
        assertEquals("4", sendMessage.getChatId());
    }

    @Test
    public void freeText() {
        when(message.getText()).thenReturn("Hello world");
        when(message.getChatId()).thenReturn(6L);
        final SendMessage sendMessage = (SendMessage) telegramWebhookBot.onWebhookUpdateReceived(update);
        final String text = sendMessage.getText();
        assertEquals("Currently, I can't deal with user free text yet.", text);
        assertEquals("6", sendMessage.getChatId());
    }

    @Test
    public void remove() {
        when(message.getText()).thenReturn("/remove");
        when(message.getChatId()).thenReturn(5L);
        final SendMessage sendMessage = (SendMessage) telegramWebhookBot.onWebhookUpdateReceived(update);
        final String text = sendMessage.getText();
        assertEquals("Have successfully removed.", text);
        assertEquals("5", sendMessage.getChatId());
    }

    @Test
    public void invite() {
        when(message.getText()).thenReturn("/invite");
        when(message.getChatId()).thenReturn(7L);
        final SendMessage sendMessage = (SendMessage) telegramWebhookBot.onWebhookUpdateReceived(update);
        final String text = sendMessage.getText();
        assertEquals("You invited to 'aaa', 'bbb", text);
        assertEquals("7", sendMessage.getChatId());
    }

    /*

    /start
    /help
    /remove
    /invite

     */
}