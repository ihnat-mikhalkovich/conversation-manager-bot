package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GroupCreatedWithBotCommand extends AbstractBotCommand {
    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        final String text = "Currently, I can't deal with group in 'group' status. Please, promote the group to 'supergroup'.";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.GROUP_CREATED_WITH_BOT;
    }
}
