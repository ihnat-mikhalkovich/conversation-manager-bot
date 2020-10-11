package com.conversation.manager.bot.util.key;

import com.conversation.manager.bot.telegram.command.extractor.impl.KeyWordPartExtractor;
import com.conversation.manager.bot.util.digest.KeyWordDigester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KeyPartRecognizerImpl implements KeyPartRecognizer {

    private final KeyWordDigester keyWordDigester;

    private final KeyWordPartExtractor keyWordPartExtractor;

    @Override
    public String recognize(Update update) {
        final String text = update.getMessage().getText();
        final Optional<String> extract = keyWordPartExtractor.extract(text);

        if (extract.isPresent()) {
            final String keyWord = extract.get();
            return keyWordDigester.digest(keyWord);
        } else {
            return keyWordDigester.digest("");
        }
    }
}
