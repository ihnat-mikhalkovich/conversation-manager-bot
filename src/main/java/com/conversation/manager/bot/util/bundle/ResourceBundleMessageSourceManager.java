package com.conversation.manager.bot.util.bundle;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ResourceBundleMessageSourceManager {
    String findMessage(Update update, String code);

    String findMessage(Update update, String code, Object[] args);
}
