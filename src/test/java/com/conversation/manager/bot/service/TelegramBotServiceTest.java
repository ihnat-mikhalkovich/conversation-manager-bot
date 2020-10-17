package com.conversation.manager.bot.service;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.service.prepare.PreparedRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.function.SupplierUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TelegramBotServiceTest {

    @Autowired
    private TelegramWebhookBot telegramWebhookBot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @MockBean
    private PreparedRequestService preparedRequestService;

    @Mock
    private org.telegram.telegrambots.meta.api.objects.User from;

    @Test
    public void fieldSetCorrectly() {
        assertEquals("the_bot_name", telegramWebhookBot.getBotUsername());
        assertEquals("the_bot_path", telegramWebhookBot.getBotPath());
        assertEquals("the_bot_token", telegramWebhookBot.getBotToken());
    }

    @BeforeEach
    public void setUpUpdate() {
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getFrom()).thenReturn(from);
        when(update.hasMessage()).thenReturn(true);

        when(update.getMessage().getFrom().getId()).thenReturn(1);
        when(message.getChatId()).thenReturn(1L);
        when(message.hasText()).thenReturn(true);

        final Chat chatMock = mock(Chat.class);
        when(chatMock.isUserChat()).thenReturn(true);

        when(update.getMessage().getChat()).thenReturn(chatMock);
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
        assertEquals("I haven't got something to remove.", text);
        assertEquals("5", sendMessage.getChatId());
    }

    @Test
    @Transactional
    public void invite() {
        when(message.getText()).thenReturn("/invite");
        when(message.getChatId()).thenReturn(1101L);
        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));
        final Set<Chat> chats = new HashSet<>();
        chats.add(SupplierUtils.resolve(() -> {
            final Chat chat = mock(Chat.class);
            when(chat.getTitle()).thenReturn("Test chat 1");
            when(chat.getInviteLink()).thenReturn("https://t.me/test-chat-1");
            when(chat.getId()).thenReturn(201L);
            return chat;
        }));
        when(preparedRequestService.findChats(groups)).thenReturn(chats);
        when(preparedRequestService.unban(201L, 1101)).thenReturn(true);
        final SendMessage sendMessage = (SendMessage) telegramWebhookBot.onWebhookUpdateReceived(update);
        final String text = sendMessage.getText();
        assertEquals("Invite links: <a href=\"https://t.me/test-chat-1\">Test chat 1</a>.", text);
        assertEquals("1101", sendMessage.getChatId());
    }

    @Test
    public void doNothing() {
        final Update updateMock = mock(Update.class);
        when(updateMock.getMessage()).thenReturn(null);
        when(updateMock.hasMessage()).thenReturn(false);

        final BotApiMethod<?> botApiMethod = telegramWebhookBot.onWebhookUpdateReceived(updateMock);

        assertNull(botApiMethod);
    }
}