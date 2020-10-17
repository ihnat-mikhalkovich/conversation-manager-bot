package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.service.prepare.PreparedRequestService;
import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import com.conversation.manager.bot.util.StringUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Component
public class RemoveCommand extends AbstractBotCommand {

    @Override
    protected BotApiMethod<?> process(Long userChatId, Update update) {
        final Integer userId = update.getMessage().getFrom().getId();

        User user = new User();
        user.setUserId(userId);
        user.setHashKey(keyPartRecognizer.recognize(update));
        final Set<Group> userGroups = preparedRequestService.findGroupsByUserId(userId);

        final StringBuilder positiveSection = new StringBuilder("Have successfully removed: ");
        final Iterator<Group> iterator = userGroups.iterator();
        while (iterator.hasNext()) {
            final Group group = iterator.next();
            final boolean isSuccess = preparedRequestService.kickFromChat(group.getGroupId(), userId);
            if (isSuccess) {
                final Optional<Chat> chat = preparedRequestService.findChat(group.getGroupId());
                if (chat.isPresent()) {
                    positiveSection.append(chat.get().getTitle()).append(',');
                } else {
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }

        final StringBuilder result = new StringBuilder();
        if (userGroups.size() > 0) {
            user.getGroups().addAll(userGroups);
            final Optional<User> optUser = userRepository.findByUserIdAndHashKey(user.getUserId(), user.getHashKey());
            if (optUser.isPresent()) {
                user = optUser.get();
                user.getGroups().addAll(userGroups);
            }
            userRepository.saveAndFlush(user);
            final String positiveResult = StringUtil.replaceLastCharWithDot(positiveSection.toString());
            result.append(positiveResult);
        } else {
            result.append("I haven't got something to remove.");
        }

        return new SendMessage(userChatId, result.toString());
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.REMOVE;
    }
}
