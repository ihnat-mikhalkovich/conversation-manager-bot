package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.service.prepare.PrepareRequestService;
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
public class RemoveCommand extends AbstractBotCommand {

    protected PrepareRequestService prepareRequestService;

    @Autowired
    public void setPrepareRequestService(PrepareRequestService prepareRequestService) {
        this.prepareRequestService = prepareRequestService;
    }

    @Override
    protected BotApiMethod<?> process(Long userChatId, Update update) {
        final Integer userId = update.getMessage().getFrom().getId();

        final User user = new User();
        user.setUserId(userId);
        user.setHashKey(keyPartRecognizer.recognize(update));
        user.getGroups().addAll(prepareRequestService.findGroupsByUserId(userId));

        userRepository.saveAndFlush(user);

        return new SendMessage(userChatId, "Have successfully removed.");
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.REMOVE;
    }
}
