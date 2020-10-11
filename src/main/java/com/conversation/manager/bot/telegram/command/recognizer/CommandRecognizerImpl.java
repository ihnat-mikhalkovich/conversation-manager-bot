package com.conversation.manager.bot.telegram.command.recognizer;

import com.conversation.manager.bot.telegram.command.BotCommandType;
import com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CommandRecognizerImpl extends AbstractCommandRecognizer {

    @Autowired
    public CommandRecognizerImpl(CommandPartExtractor commandPartExtractor) {
        super(commandPartExtractor);
    }

    @Override
    public BotCommandType process(String text) {
        final Optional<String> extract = commandPartExtractor.extract(text);
        if (!extract.isPresent()) {
            return BotCommandType.NO_COMMAND;
        }

        final String command = extract.get();
        return parse(command);
    }

    private BotCommandType parse(String command) {
        return Arrays.stream(BotCommandType.values())
                .filter(value -> value.toString().equalsIgnoreCase(command))
                .findFirst().orElse(BotCommandType.UNRECOGNIZED);
    }
}
