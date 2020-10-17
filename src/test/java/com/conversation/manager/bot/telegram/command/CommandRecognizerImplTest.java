package com.conversation.manager.bot.telegram.command;

import com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor;
import com.conversation.manager.bot.telegram.command.recognizer.CommandRecognizerImpl;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandRecognizerImplTest {

    final CommandPartExtractor commandPartExtractor = mock(CommandPartExtractor.class);
    final CommandRecognizerImpl commandRecognizer
            = new CommandRecognizerImpl(commandPartExtractor);

    @Test
    public void testHelpCommand() {
        when(commandPartExtractor.extract("/help")).thenReturn(Optional.of("help"));
        final BotCommandType type = commandRecognizer.process("/help");
        assertEquals(BotCommandType.HELP, type);
    }

    @Test
    public void testStartCommand() {
        when(commandPartExtractor.extract("/start")).thenReturn(Optional.of("start"));
        final BotCommandType type = commandRecognizer.process("/start");
        assertEquals(BotCommandType.START, type);
    }

    @Test
    public void testInviteCommand() {
        when(commandPartExtractor.extract("/invite please")).thenReturn(Optional.of("invite"));
        final BotCommandType type = commandRecognizer.process("/invite please");
        assertEquals(BotCommandType.INVITE, type);
    }

    @Test
    public void testRemoveCommand() {
        when(commandPartExtractor.extract("/remove please")).thenReturn(Optional.of("remove"));
        final BotCommandType type = commandRecognizer.process("/remove please");
        assertEquals(BotCommandType.REMOVE, type);
    }

    @Test
    public void testBadCommand() {
        when(commandPartExtractor.extract("/bla-bla-bla please")).thenReturn(Optional.of("bla-bla-bla"));
        final BotCommandType type = commandRecognizer.process("/bla-bla-bla please");
        assertEquals(BotCommandType.UNRECOGNIZED, type);
    }

    @Test
    public void testDoNothingCommandWhenHasNotMessage() {
        final Update updateMock = mock(Update.class);
        when(updateMock.getMessage()).thenReturn(null);
        when(updateMock.hasMessage()).thenReturn(false);

        final BotCommandType type = commandRecognizer.recognize(updateMock);
        assertEquals(BotCommandType.DO_NOTHING, type);
    }

    @Test
    public void testDoNothingCommandWhenHasNotText() {
        final Update updateMock = mock(Update.class);

        final Message messageMock = mock(Message.class);
        when(messageMock.hasText()).thenReturn(false);
        when(messageMock.getText()).thenReturn(null);

        when(updateMock.getMessage()).thenReturn(messageMock);
        when(updateMock.hasMessage()).thenReturn(true);

        final BotCommandType type = commandRecognizer.recognize(updateMock);
        assertEquals(BotCommandType.DO_NOTHING, type);
    }

    @Test
    public void testBotAddedToGroup() {
        final User userMock = mock(User.class);
        when(userMock.getBot()).thenReturn(true);
        when(userMock.getUserName()).thenReturn("test-bot");
        commandRecognizer.setBotUsername("test-bot");

        final Message messageMock = mock(Message.class);
        final List<User> users = Collections.singletonList(userMock);
        when(messageMock.getNewChatMembers()).thenReturn(users);

        final Update updateMock = mock(Update.class);
        when(updateMock.hasMessage()).thenReturn(true);
        when(updateMock.getMessage()).thenReturn(messageMock);

        final BotCommandType type = commandRecognizer.recognize(updateMock);

        assertEquals(BotCommandType.BOT_ADDED_TO_GROUP, type);
    }

    @Test
    public void freeTextMessageFromSupergroup() {
        final Update updateMock = mock(Update.class);

        final Message messageMock = mock(Message.class);
        when(messageMock.hasText()).thenReturn(true);

        when(updateMock.getMessage()).thenReturn(messageMock);

        final Chat chatMock = mock(Chat.class);
        when(chatMock.isUserChat()).thenReturn(false);

        when(updateMock.getMessage().getChat()).thenReturn(chatMock);
        when(updateMock.hasMessage()).thenReturn(true);

        final BotCommandType type = commandRecognizer.recognize(updateMock);
        assertEquals(BotCommandType.DO_NOTHING, type);
    }

    @Test
    public void migrateFromGroupToSupergroupMessage() {
        final Update updateMock = mock(Update.class);
        final Message messageMock = mock(Message.class);

        when(updateMock.hasMessage()).thenReturn(true);
        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getMigrateToChatId()).thenReturn(1101L);

        final BotCommandType type = commandRecognizer.recognize(updateMock);
        assertEquals(BotCommandType.GROUP_MIGRATE_TO_SUPERGROUP, type);
    }

    @Test
    public void groupCreatedWithBotTest() {
        final Update updateMock = mock(Update.class);
        final Message messageMock = mock(Message.class);

        when(updateMock.hasMessage()).thenReturn(true);
        when(updateMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getGroupchatCreated()).thenReturn(true);

        final BotCommandType type = commandRecognizer.recognize(updateMock);
        assertEquals(BotCommandType.GROUP_CREATED_WITH_BOT, type);
    }
}