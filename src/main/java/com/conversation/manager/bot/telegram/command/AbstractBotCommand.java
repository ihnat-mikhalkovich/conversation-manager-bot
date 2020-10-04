package com.conversation.manager.bot.telegram.command;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Getter
public abstract class AbstractBotCommand implements BotCommand {

    protected AbsSender sender;

    @Autowired
    public void setAbsSender(AbsSender absSender) {
        this.sender = absSender;
    }

    @Override
    public BotApiMethod<?> execute(Update update) {
        final Long chatId = update.getMessage().getChatId();
        return process(chatId, update);
    }

    abstract protected BotApiMethod<?> process(Long chatId, Update update);
}
