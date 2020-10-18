package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class NoCommand extends AbstractBotCommand {

    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        final String text = "I haven't been able to answer on not command message. More details: /help";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.NO_COMMAND;
    }
}
