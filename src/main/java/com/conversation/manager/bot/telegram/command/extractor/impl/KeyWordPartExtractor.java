package com.conversation.manager.bot.telegram.command.extractor.impl;

import com.conversation.manager.bot.telegram.command.extractor.AbstractMessagePartExtractor;
import com.conversation.manager.bot.telegram.command.extractor.ExtractionPartType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor.FIRST_SYMBOL;

@Component
public class KeyWordPartExtractor extends AbstractMessagePartExtractor {

    @Override
    public Optional<String> process(String[] parts) {
        final List<String> collect = Arrays.stream(parts)
                .filter(part -> !StringUtils.isEmpty(part))
                .collect(Collectors.toList());

        final Iterator<String> iterator = collect.iterator();
        boolean commandFound = false;
        while (iterator.hasNext() && !commandFound) {
            final String next = iterator.next();
            if (FIRST_SYMBOL == next.charAt(0)) {
                commandFound = true;
            }
        }

        if (!iterator.hasNext()) {
            return Optional.empty();
        }

        return Optional.of(iterator.next());
    }

    @Override
    public ExtractionPartType getType() {
        return ExtractionPartType.KEY_WORD;
    }
}
