package com.conversation.manager.bot.telegram.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractBotCommand implements BotCommand {

    @Override
    public BotApiMethod<?> execute(Update update) {
        final Long chatId = update.getMessage().getChatId();
        return process(chatId, update);
    }

    abstract protected BotApiMethod<?> process(Long chatId, Update update);
}
