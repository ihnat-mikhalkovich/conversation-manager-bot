package com.conversation.manager.bot.telegram.command.extractor;

import java.util.Optional;

public abstract class AbstractMessagePartExtractor implements MessagePartExtractor {

    public static final String WORD_DELIMITER = " ";

    @Override
    public Optional<String> extract(String text) {
        final String[] split = text.trim().split(WORD_DELIMITER);
        return process(split);
    }

    public abstract Optional<String> process(String[] parts);
}
