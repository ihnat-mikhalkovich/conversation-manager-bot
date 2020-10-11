package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.service.prepare.PrepareRequestService;
import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Getter
public class InviteCommand extends AbstractBotCommand {

    private PrepareRequestService prepareRequestService;

    @Autowired
    public void setPrepareRequestService(PrepareRequestService prepareRequestService) {
        this.prepareRequestService = prepareRequestService;
    }

    @Override
    protected BotApiMethod<?> process(Long userChatId, Update update) {
        final Integer userId = userChatId.intValue();

        final List<User> users = this.findAppropriateUserList(userId, update);
        if (users.size() == 0) {
            return new SendMessage(userChatId, "I don't know this key word.");
        }

        final User user = users.iterator().next();
        final Set<Group> groups = user.getGroups();

        final Set<Chat> chats = prepareRequestService.findChats(groups);

        final Pair<Set<Chat>, Set<Chat>> unbanedAndBaned = this.separateOnUnbanedAndBaned(chats, userId);

        removeGroups(unbanedAndBaned, userId);

        final String resultMessage = this.makeResultMessage(unbanedAndBaned);
        return new SendMessage(userChatId, resultMessage);
    }

    @Transactional
    private void removeGroups(Pair<Set<Chat>, Set<Chat>> unbanedAndBaned, Integer userId) {
        final Set<Chat> baned = unbanedAndBaned.getSecond();
        if (baned.isEmpty()) {
            userRepository.deleteById(userId);
            return;
        }
        final Set<Chat> unbaned = unbanedAndBaned.getFirst();
        final Set<Long> unbandedGroupIds = unbaned.stream().map(Chat::getId).collect(Collectors.toSet());
        final User one = userRepository.getOne(userId);
        final Set<Group> newUserGroups = one.getGroups().stream()
                .filter(group -> !unbandedGroupIds.contains(group.getGroupId()))
                .collect(Collectors.toSet());
        one.setGroups(newUserGroups);
        userRepository.saveAndFlush(one);
    }

    private Pair<Set<Chat>, Set<Chat>> separateOnUnbanedAndBaned(Set<Chat> chats, Integer userId) {
        final Set<Chat> unbaned = new HashSet<>();
        final Set<Chat> baned = new HashSet<>();
        for (Chat chat : chats) {
            final boolean unban = prepareRequestService.unban(chat.getId(), userId);
            if (unban) {
                unbaned.add(chat);
            } else {
                baned.add(chat);
            }
        }
        return Pair.of(unbaned, baned);
    }

    private String makeResultMessage(Pair<Set<Chat>, Set<Chat>> unbanedAndBaned) {
        final Set<Chat> unbaned = unbanedAndBaned.getFirst();
        final Set<Chat> baned = unbanedAndBaned.getSecond();

        String result = "";
        if (!unbaned.isEmpty()) {
            result = "Invite links:";
            for (Chat chat : unbaned) {
                final String link = " <a href=\""
                        + chat.getInviteLink()
                        + "\">"
                        + chat.getTitle()
                        + "</a>";
                result = result.concat(link).concat(",");
            }
            result = this.replaceLastCharWithDot(result);
        }

        if (!unbaned.isEmpty() && !baned.isEmpty()) {
            result = result.concat("\n");
        }

        if (!baned.isEmpty()) {
            result = result + "I can't unban:";
            for (Chat chat : baned) {
                result = result.concat(" ")
                        .concat(chat.getTitle())
                        .concat(",");
            }
            result = this.replaceLastCharWithDot(result);
        }
        return result;
    }

    private String replaceLastCharWithDot(String str) {
        return str.substring(0, str.length() - 1).concat(".");
    }

    private List<User> findAppropriateUserList(Integer userId, Update update) {
        final String hash = keyPartRecognizer.recognize(update);
        return userRepository.findByUserIdAndHashKey(userId, hash);
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.INVITE;
    }
}
