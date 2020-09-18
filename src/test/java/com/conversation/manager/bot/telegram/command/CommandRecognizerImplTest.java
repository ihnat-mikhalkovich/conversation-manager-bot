package com.conversation.manager.bot.telegram.command;

import com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor;
import com.conversation.manager.bot.telegram.command.recognizer.CommandRecognizerImpl;
import org.junit.jupiter.api.Test;

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
}