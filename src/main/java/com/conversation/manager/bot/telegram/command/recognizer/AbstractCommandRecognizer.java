package com.conversation.manager.bot.telegram.command.recognizer;

import com.conversation.manager.bot.telegram.command.BotCommandType;
import com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCommandRecognizer implements CommandRecognizer {

    protected final CommandPartExtractor commandPartExtractor;

    @Override
    public BotCommandType recognize(String text) {
        return this.process(text);
    }

    protected abstract BotCommandType process(String text);
}
