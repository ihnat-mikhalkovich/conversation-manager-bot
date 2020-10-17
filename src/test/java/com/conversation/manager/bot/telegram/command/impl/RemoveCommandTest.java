package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.repository.UserRepository;
import com.conversation.manager.bot.service.prepare.PreparedRequestService;
import com.conversation.manager.bot.telegram.method.ChatWithEquals;
import org.assertj.core.api.Assertions;
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
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
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
    private PreparedRequestService preparedRequestService;

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
    public void removeFromOneGroup() {
        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));
        when(preparedRequestService.findGroupsByUserId(101)).thenReturn(groups);

        when(update.getMessage().getFrom().getId()).thenReturn(101);
        when(message.getChatId()).thenReturn(101L);
        when(message.getText()).thenReturn("/remove");

        final User expectedUser = new User();
        expectedUser.setId(4L);
        expectedUser.setUserId(101);
        expectedUser.setHashKey(DigestUtils.md5DigestAsHex("".getBytes()));
        expectedUser.getGroups().add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));

        final Chat chatMock = mock(Chat.class);
        when(chatMock.getId()).thenReturn(201L);
        when(chatMock.getTitle()).thenReturn("Test1");
        when(preparedRequestService.findChat(201L)).thenReturn(Optional.of(chatMock));

        when(preparedRequestService.kickFromChat(201L, 101)).thenReturn(true);

        final BotApiMethod<?> method = removeCommand.execute(update);
        final SendMessage sendMessage = (SendMessage) method;
        final String resultText = sendMessage.getText();
        assertEquals("Have successfully removed: Test1.", resultText);

        final Optional<User> optOne = userRepository.findByUserIdAndHashKey(101, expectedUser.getHashKey());
        assertTrue(optOne.isPresent());
        final User user = optOne.get();
        assertEquals(expectedUser.getHashKey(), user.getHashKey());
        assertEquals(expectedUser.getUserId(), user.getUserId());
        assertEquals(expectedUser.getGroups(), user.getGroups());
        verify(preparedRequestService, times(1)).kickFromChat(201L, 101);
    }

    @Test
    public void removeFromTwoGroups() {
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
        when(preparedRequestService.findGroupsByUserId(102)).thenReturn(groups);

        when(update.getMessage().getFrom().getId()).thenReturn(102);
        when(message.getChatId()).thenReturn(102L);
        when(message.getText()).thenReturn("/remove cat");

        final User expectedUser = new User();
        expectedUser.setId(4L);
        expectedUser.setUserId(102);
        expectedUser.setHashKey(DigestUtils.md5DigestAsHex("cat".getBytes()));
        final Set<Group> expectedGroups = groups.stream()
                .filter(group -> group.getGroupId() == 202L)
                .collect(Collectors.toSet());
        expectedUser.getGroups().addAll(expectedGroups);

        final Chat chatMock = mock(Chat.class);
        when(chatMock.getId()).thenReturn(201L);
        when(chatMock.getTitle()).thenReturn("Test1");
        when(preparedRequestService.findChat(201L)).thenReturn(Optional.of(chatMock));

        final Chat chatMock2 = mock(Chat.class);
        when(chatMock2.getId()).thenReturn(201L);
        when(chatMock2.getTitle()).thenReturn("Test2");
        when(preparedRequestService.findChat(202L)).thenReturn(Optional.of(chatMock2));

        when(preparedRequestService.kickFromChat(201L, 102)).thenReturn(false);
        when(preparedRequestService.kickFromChat(202L, 102)).thenReturn(true);

        final BotApiMethod<?> method = removeCommand.execute(update);
        final SendMessage sendMessage = (SendMessage) method;
        final String resultText = sendMessage.getText();
        assertEquals("Have successfully removed: Test2.", resultText);

        final Optional<User> optOne = userRepository.findByUserIdAndHashKey(102, expectedUser.getHashKey());
        assertTrue(optOne.isPresent());
        final User user = optOne.get();
        assertEquals(expectedUser.getHashKey(), user.getHashKey());
        assertEquals(expectedUser.getUserId(), user.getUserId());
        assertEquals(expectedUser.getGroups(), user.getGroups());
        verify(preparedRequestService, times(1)).kickFromChat(201L, 102);
        verify(preparedRequestService, times(1)).kickFromChat(202L, 102);
    }

    @Test
    public void removeFromNoGroups() {
        when(preparedRequestService.findGroupsByUserId(101)).thenReturn(new HashSet<>());

        when(update.getMessage().getFrom().getId()).thenReturn(101);
        when(message.getChatId()).thenReturn(101L);
        when(message.getText()).thenReturn("/remove");

        when(preparedRequestService.kickFromChat(201L, 101)).thenReturn(false);

        final BotApiMethod<?> method = removeCommand.execute(update);
        final SendMessage sendMessage = (SendMessage) method;
        final String resultText = sendMessage.getText();
        assertEquals("I haven't got something to remove.", resultText);

        final Optional<User> optOne = userRepository.findByUserIdAndHashKey(101, "d41d8cd98f00b204e9800998ecf8427e");
        assertFalse(optOne.isPresent());

        verify(preparedRequestService, times(0)).kickFromChat(201L, 101);
    }

    @Test
    public void removeFromOneGroupButYouCantKick() {
        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));
        when(preparedRequestService.findGroupsByUserId(101)).thenReturn(groups);

        when(update.getMessage().getFrom().getId()).thenReturn(101);
        when(message.getChatId()).thenReturn(101L);
        when(message.getText()).thenReturn("/remove");

        final User expectedUser = new User();
        expectedUser.setId(4L);
        expectedUser.setUserId(101);
        expectedUser.setHashKey(DigestUtils.md5DigestAsHex("".getBytes()));
        expectedUser.getGroups().add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));

        final Chat chatMock = mock(Chat.class);
        when(chatMock.getId()).thenReturn(201L);
        when(chatMock.getTitle()).thenReturn("Test1");
        when(preparedRequestService.findChat(201L)).thenReturn(Optional.of(chatMock));

        when(preparedRequestService.kickFromChat(201L, 101)).thenReturn(false);

        final BotApiMethod<?> method = removeCommand.execute(update);
        final SendMessage sendMessage = (SendMessage) method;
        final String resultText = sendMessage.getText();
        assertEquals("I haven't got something to remove.", resultText);

        final Optional<User> optOne = userRepository.findByUserIdAndHashKey(101, expectedUser.getHashKey());
        assertFalse(optOne.isPresent());
        verify(preparedRequestService, times(1)).kickFromChat(201L, 101);
    }

    @Test
    public void testRemoveWhenNeedToAddGroupToExistedUser() {
        when(update.getMessage().getFrom().getId()).thenReturn(1101);
        when(message.getChatId()).thenReturn(1101L);

        when(preparedRequestService.kickFromChat(202L, 1101)).thenReturn(true);

        final Chat chatMock = mock(Chat.class);
        when(chatMock.getId()).thenReturn(202L);
        when(chatMock.getTitle()).thenReturn("Test2");
        when(preparedRequestService.findChat(202L)).thenReturn(Optional.of(chatMock));

        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(3L);
            group.setGroupId(202L);
            return group;
        }));
        when(preparedRequestService.findGroupsByUserId(1101)).thenReturn(groups);

        final SendMessage sendMessage = (SendMessage) removeCommand.execute(update);

        final User expectedUser = new User();
        expectedUser.setId(2L);
        expectedUser.setUserId(1101);
        expectedUser.setHashKey("d41d8cd98f00b204e9800998ecf8427e");
        expectedUser.getGroups().add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(2L);
            group.setGroupId(201L);
            return group;
        }));
        expectedUser.getGroups().add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setId(3L);
            group.setGroupId(202L);
            return group;
        }));

        final Optional<User> optUser = userRepository.findByUserIdAndHashKey(1101, "d41d8cd98f00b204e9800998ecf8427e");
        assertThat(optUser.isPresent()).isTrue();
        final User user = optUser.get();
        assertThat(expectedUser.getGroups()).isEqualTo(user.getGroups());
    }
}