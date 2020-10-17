package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
public class ChatIsNotSupergroupCommand extends AbstractBotCommand {

    private DoNothingCommand doNothingCommand;

    @Autowired
    public void setDoNothingCommand(DoNothingCommand doNothingCommand) {
        this.doNothingCommand = doNothingCommand;
    }

    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        final String text = "Currently, I can't deal with chats in 'group' status. Please, promote the chat to 'supergroup'.";
        preparedRequestService.sendMessage(chatId, text);
        final boolean isSuccess = preparedRequestService.leaveChat(chatId);

        if (isSuccess) {
            return doNothingCommand.process(chatId, update);
        } else {
            final String newText = "Please, remove me from this chat and add again.";
            return new SendMessage(chatId, newText);
        }
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.CHAT_IS_NOT_SUPERGROUP;
    }
}
