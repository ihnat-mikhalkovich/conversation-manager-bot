package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RemoveCommand extends AbstractBotCommand {
    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        return new SendMessage(chatId, "Have successfully removed.");
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.REMOVE;
    }
}
