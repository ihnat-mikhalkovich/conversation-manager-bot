package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand extends AbstractBotCommand {

    private static final String MESSAGE = "Hi, nice to meet you. I'm a conversation manager. You can be easily removed from the 'supergroup' and invited again by me. For more information, please, enter /help command.";

    @Override
    public SendMessage process(Long chatId, Update update) {
        return new SendMessage(chatId, MESSAGE);
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.START;
    }
}
