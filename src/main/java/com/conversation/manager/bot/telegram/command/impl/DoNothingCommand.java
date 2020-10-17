package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DoNothingCommand extends AbstractBotCommand {
    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        return null;
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.DO_NOTHING;
    }
}
