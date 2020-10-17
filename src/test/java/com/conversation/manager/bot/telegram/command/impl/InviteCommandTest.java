package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.repository.UserRepository;
import com.conversation.manager.bot.service.prepare.PreparedRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.function.SupplierUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class InviteCommandTest {

    @Autowired
    private InviteCommand inviteCommand;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private org.telegram.telegrambots.meta.api.objects.User from;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PreparedRequestService preparedRequestService;

    @BeforeEach
    public void setupUpdate() {
        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getFrom()).thenReturn(from);
        when(update.hasMessage()).thenReturn(true);

        when(update.getMessage().getFrom().getId()).thenReturn(1);
        when(message.getChatId()).thenReturn(1L);
        when(message.hasText()).thenReturn(true);
    }

    @Test
    public void inviteToOneGroup() {
        when(update.getMessage().getFrom().getId()).thenReturn(1101);
        when(message.getChatId()).thenReturn(1101L);
        when(message.getText()).thenReturn("/invite");
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

        final SendMessage sendMessage = (SendMessage) inviteCommand.execute(update);
        final String text = sendMessage.getText();
        assertEquals("Invite links: <a href=\"https://t.me/test-chat-1\">Test chat 1</a>.", text);

        final Integer userId = update.getMessage().getFrom().getId();
        final Optional<User> optUser = userRepository.findByUserIdAndHashKey(userId, "d41d8cd98f00b204e9800998ecf8427e");
        assertFalse(optUser.isPresent());
    }

    @Test
    public void checkKeyWord() {
        when(update.getMessage().getFrom().getId()).thenReturn(1101);
        when(message.getChatId()).thenReturn(1101L);
        when(message.getText()).thenReturn("/invite password");

        final SendMessage sendMessage = (SendMessage) inviteCommand.execute(update);
        final String text = sendMessage.getText();
        assertEquals("I don't know this key word.", text);
    }

    @Test
    public void inviteToTwoGroups() {
        when(update.getMessage().getFrom().getId()).thenReturn(1102);
        when(message.getChatId()).thenReturn(1102L);
        when(message.getText()).thenReturn("/invite password");
        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(3L);
            group.setGroupId(202L);
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
        chats.add(SupplierUtils.resolve(() -> {
            final Chat chat = mock(Chat.class);
            when(chat.getTitle()).thenReturn("Test chat 2");
            when(chat.getInviteLink()).thenReturn("https://t.me/test-chat-2");
            when(chat.getId()).thenReturn(202L);
            return chat;
        }));
        when(preparedRequestService.findChats(groups)).thenReturn(chats);
        when(preparedRequestService.unban(201L, 1102)).thenReturn(true);
        when(preparedRequestService.unban(202L, 1102)).thenReturn(true);

        final SendMessage sendMessage = (SendMessage) inviteCommand.execute(update);
        final String text = sendMessage.getText();
        assertTrue("Invite links: <a href=\"https://t.me/test-chat-1\">Test chat 1</a>, <a href=\"https://t.me/test-chat-2\">Test chat 2</a>.".equals(text)
                || "Invite links: <a href=\"https://t.me/test-chat-2\">Test chat 2</a>, <a href=\"https://t.me/test-chat-1\">Test chat 1</a>.".equals(text));

        final Integer userId = update.getMessage().getFrom().getId();
        final Optional<User> optUser = userRepository.findByUserIdAndHashKey(userId, "5f4dcc3b5aa765d61d8327deb882cf99");
        assertFalse(optUser.isPresent());
    }

    @Test
    public void checkUnBan() {
        when(update.getMessage().getFrom().getId()).thenReturn(1101);
        when(message.getChatId()).thenReturn(1101L);
        when(message.getText()).thenReturn("/invite");
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
        when(preparedRequestService.unban(201L, 1101)).thenReturn(false);

        final SendMessage sendMessage = (SendMessage) inviteCommand.execute(update);
        final String text = sendMessage.getText();
        assertEquals("I can't unban: Test chat 1.", text);
        final Integer userId = update.getMessage().getFrom().getId();
        final Optional<User> optUser = userRepository.findByUserIdAndHashKey(userId, DigestUtils.md5DigestAsHex("".getBytes()));
        assertTrue(optUser.isPresent());
    }

    @Test
    public void checkUnBanWithTwoGroups() {
        when(update.getMessage().getFrom().getId()).thenReturn(1102);
        when(message.getChatId()).thenReturn(1102L);
        when(message.getText()).thenReturn("/invite password");
        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(3L);
            group.setGroupId(202L);
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
        chats.add(SupplierUtils.resolve(() -> {
            final Chat chat = mock(Chat.class);
            when(chat.getTitle()).thenReturn("Test chat 2");
            when(chat.getInviteLink()).thenReturn("https://t.me/test-chat-2");
            when(chat.getId()).thenReturn(202L);
            return chat;
        }));
        when(preparedRequestService.findChats(groups)).thenReturn(chats);
        when(preparedRequestService.unban(201L, 1102)).thenReturn(false);
        when(preparedRequestService.unban(202L, 1102)).thenReturn(true);

        final SendMessage sendMessage = (SendMessage) inviteCommand.execute(update);
        final String text = sendMessage.getText();
        assertEquals("Invite links: <a href=\"https://t.me/test-chat-2\">Test chat 2</a>.\n" +
                "I can't unban: Test chat 1.", text);
        final Integer userId = update.getMessage().getFrom().getId();
        final Optional<User> optUser = userRepository.findByUserIdAndHashKey(userId, "5f4dcc3b5aa765d61d8327deb882cf99");
        assertTrue(optUser.isPresent());
    }
}