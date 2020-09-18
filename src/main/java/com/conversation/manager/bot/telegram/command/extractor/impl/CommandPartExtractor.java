package com.conversation.manager.bot.telegram.command.extractor.impl;

import com.conversation.manager.bot.telegram.command.extractor.AbstractMessagePartExtractor;
import com.conversation.manager.bot.telegram.command.extractor.ExtractionPartType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CommandPartExtractor extends AbstractMessagePartExtractor {

    public static final char FIRST_SYMBOL = '/';

    @Override
    public Optional<String> process(String[] parts) {
        return Arrays.stream(parts)
                .filter(part -> !StringUtils.isEmpty(part))
                .filter(part -> FIRST_SYMBOL == part.charAt(0))
                .map(part -> part.substring(1))
                .findFirst();
    }

    @Override
    public ExtractionPartType getType() {
        return ExtractionPartType.COMMAND;
    }
}
