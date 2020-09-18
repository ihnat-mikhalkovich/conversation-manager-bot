package com.conversation.manager.bot.telegram.command.recognizer;

import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandRecognizer {

    default BotCommandType recognize(Update update) {
        if (!update.hasMessage()) {
            return BotCommandType.UNRECOGNIZED;
        }
        final Message message = update.getMessage();
        return this.recognize(message);
    }

    default BotCommandType recognize(Message message) {
        if (!message.hasText()) {
            return BotCommandType.UNRECOGNIZED;
        }
        final String text = message.getText();
        return this.recognize(text);
    }

    BotCommandType recognize(String text);
}
