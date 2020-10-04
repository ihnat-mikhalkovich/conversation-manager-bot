package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RemoveCommandTest {

    @Autowired
    private RemoveCommand removeCommand;

    @SpyBean
    private TelegramWebhookBot telegramWebhookBot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @BeforeEach
    public void setupUpdate() {
        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage().getFrom().getId()).thenReturn(1);
        when(message.getChatId()).thenReturn(1L);
        when(message.hasText()).thenReturn(true);

        when(message.getText()).thenReturn("/remove");
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void test() {

        // find chats where t-user participate in your pull
        // remove from there


        final BotApiMethod<?> execute = removeCommand.execute(update);
        final List<User> all = userRepository.findAll();
        final User next = all.iterator().next();
        System.out.println(next.getChats());
        assertTrue(next.getChats().iterator().next().getChatId().equals(123L));
        System.out.println(all);
//        final String s = DigestUtils.md5DigestAsHex("nimda".getBytes());
    }


//    @Test
    @Transactional
    public void testRemove() throws TelegramApiException {
        // find chats where t-user participate in your pull
        // remove from chats
        // save in db user_id, key_value, chat_list
        final BotApiMethod<?> method = removeCommand.execute(update);

        final KickChatMember kickChatMember = new KickChatMember();
        final Integer userId = update.getMessage().getFrom().getId();
        kickChatMember.setUserId(userId);
        kickChatMember.setChatId(2L);
        verify(telegramWebhookBot, times(1)).execute(kickChatMember);
    }
}