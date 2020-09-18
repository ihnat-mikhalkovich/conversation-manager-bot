package com.conversation.manager.bot.telegram.command.extractor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class MessagePartExtractorDirector {
    private final Map<ExtractionPartType, MessagePartExtractor> commandPartExtractorMap;

    public MessagePartExtractorDirector() {
        commandPartExtractorMap = new EnumMap<>(ExtractionPartType.class);
    }

    public void register(MessagePartExtractor extractor) {
        commandPartExtractorMap.put(extractor.getType(), extractor);
    }

    public MessagePartExtractor getExtractor(ExtractionPartType type) {
        return commandPartExtractorMap.get(type);
    }
}
