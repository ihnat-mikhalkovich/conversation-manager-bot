package com.conversation.manager.bot.telegram.command.extractor;

import com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor;
import com.conversation.manager.bot.telegram.command.extractor.impl.KeyWordPartExtractor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MessagePartExtractorTest {

    final CommandPartExtractor commandPartExtractor = new CommandPartExtractor();
    final KeyWordPartExtractor keyWordPartExtractor = new KeyWordPartExtractor();

    @Test
    void extractHelpCommand() {
        final Optional<String> extract = commandPartExtractor.extract("/help");
        assertEquals("help", extract.get());
        final Optional<String> extract1 = keyWordPartExtractor.extract("/help");
        assertFalse(extract1.isPresent());
    }

    @Test
    void extractInviteCommand() {
        final Optional<String> extract = commandPartExtractor.extract("/invite    me");
        assertEquals("invite", extract.get());
        final Optional<String> extract1 = keyWordPartExtractor.extract("/invite    me");
        assertEquals("me", extract1.get());
    }

    @Test
    void extractRemoveCommand() {
        final Optional<String> extract = commandPartExtractor.extract("      /remove    me    ");
        assertEquals("remove", extract.get());
        final Optional<String> extract1 = keyWordPartExtractor.extract("      /remove    me    ");
        assertEquals("me", extract1.get());
    }

    @Test
    void extractRemoveCommand3Words() {
        final Optional<String> extract = commandPartExtractor.extract("  Hi,    /remove    me    ");
        assertEquals("remove", extract.get());
        final Optional<String> extract1 = keyWordPartExtractor.extract("  Hi,    /remove    me    ");
        assertEquals("me", extract1.get());
    }
}