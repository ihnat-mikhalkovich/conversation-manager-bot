package com.conversation.manager.bot.telegram.command.extractor;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public interface MessagePartExtractor {

    Optional<String> extract(String text);

    ExtractionPartType getType();

    @Autowired
    default void publish(MessagePartExtractorDirector director) {
        director.register(this);
    }
}
