package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.conversation.manager.bot.telegram.command.BotCommandType.BOT_ADDED_TO_SUPERGROUP;

@Component
public class BotAddedToSupergroupCommand extends AbstractBotCommand {

    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        final Group group = new Group();
        group.setGroupId(chatId);
        groupRepository.saveAndFlush(group);
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Feel free to contact me.");
        return sendMessage;
    }

    @Override
    public BotCommandType getType() {
        return BOT_ADDED_TO_SUPERGROUP;
    }
}
