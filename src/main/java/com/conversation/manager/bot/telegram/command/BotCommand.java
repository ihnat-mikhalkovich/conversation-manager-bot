package com.conversation.manager.bot.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {

    BotApiMethod<?> execute(Update update);

    BotCommandType getType();

    @Autowired
    default void publish(BotCommandDirector director) {
        director.register(this);
    }
}
