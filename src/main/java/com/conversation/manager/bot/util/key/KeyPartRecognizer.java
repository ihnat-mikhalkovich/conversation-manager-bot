package com.conversation.manager.bot.util.key;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface KeyPartRecognizer {
    String recognize(Update update);
}
