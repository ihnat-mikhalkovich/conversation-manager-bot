package com.conversation.manager.bot.telegram.command.recognizer;

import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandRecognizer {
    BotCommandType recognize(Update update);
}
