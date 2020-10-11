package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.repository.UserRepository;
import com.conversation.manager.bot.service.prepare.PrepareRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.function.SupplierUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RemoveCommandTest {

    @Autowired
    private RemoveCommand removeCommand;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private org.telegram.telegrambots.meta.api.objects.User from;

    @MockBean
    private PrepareRequestService prepareRequestService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setupUpdate() {
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getFrom()).thenReturn(from);
        when(update.hasMessage()).thenReturn(true);

        when(update.getMessage().getFrom().getId()).thenReturn(1);
        when(message.getChatId()).thenReturn(1L);
        when(message.hasText()).thenReturn(true);

        when(message.getText()).thenReturn("/remove");
    }

    // find chats where t-user participate in your pull
    // remove from chats
    // save in db user_id, key_value, chat_list

    @Test
    @Transactional
    public void removeFromOneGroup() {
        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(201L);
            return group;
        }));
        when(prepareRequestService.findGroupsByUserId(101)).thenReturn(groups);

        when(update.getMessage().getFrom().getId()).thenReturn(101);
        when(message.getChatId()).thenReturn(101L);
        when(message.getText()).thenReturn("/remove");

        final User expectedUser = new User();
        expectedUser.setUserId(101);
        expectedUser.setHashKey(DigestUtils.md5DigestAsHex("".getBytes()));
        expectedUser.getGroups().add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(201L);
            return group;
        }));

        final BotApiMethod<?> method = removeCommand.execute(update);
        final SendMessage sendMessage = (SendMessage) method;
        final String resultText = sendMessage.getText();
        assertEquals("Have successfully removed.", resultText);

        final User one = userRepository.getOne(101);
        assertEquals(expectedUser, one);
        assertEquals(expectedUser.getGroups(), one.getGroups());
    }

    @Test
    @Transactional
    public void removeFromTwoGroups() {
        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(201L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(202L);
            return group;
        }));
        when(prepareRequestService.findGroupsByUserId(102)).thenReturn(groups);

        when(update.getMessage().getFrom().getId()).thenReturn(102);
        when(message.getChatId()).thenReturn(102L);
        when(message.getText()).thenReturn("/remove cat");

        final User expectedUser = new User();
        expectedUser.setUserId(102);
        expectedUser.setHashKey(DigestUtils.md5DigestAsHex("cat".getBytes()));
        expectedUser.getGroups().addAll(groups);

        final BotApiMethod<?> method = removeCommand.execute(update);
        final SendMessage sendMessage = (SendMessage) method;
        final String resultText = sendMessage.getText();
        assertEquals("Have successfully removed.", resultText);

        final User one = userRepository.getOne(102);
        assertEquals(expectedUser, one);
        assertEquals(expectedUser.getGroups(), one.getGroups());
    }
}