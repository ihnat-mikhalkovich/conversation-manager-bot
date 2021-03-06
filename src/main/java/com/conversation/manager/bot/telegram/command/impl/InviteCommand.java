package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import com.conversation.manager.bot.util.StringUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InviteCommand extends AbstractBotCommand {

    @Override
    protected BotApiMethod<?> process(Long userChatId, Update update) {
        final Integer userId = userChatId.intValue();
        final String hash = keyPartRecognizer.recognize(update);
        final Optional<User> optUser = userRepository.findByUserIdAndHashKey(userId, hash);

        if (!optUser.isPresent()) {
            final String message = bundleMessageSourceManager.findMessage(update, "command.invite.bad-key-word");
            return new SendMessage(userChatId, message);
        }

        final User user = optUser.get();
        final Set<Group> groups = user.getGroups();

        final Set<Chat> chats = preparedRequestService.findChats(groups);

        final Pair<Set<Chat>, Set<Chat>> unbanedAndBaned = this.separateOnUnbanedAndBaned(chats, userId);

        removeGroups(unbanedAndBaned, user);

        final String resultMessage = this.makeResultMessage(unbanedAndBaned, update);
        final SendMessage sendMessage = new SendMessage(userChatId, resultMessage);
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    private void removeGroups(Pair<Set<Chat>, Set<Chat>> unbanedAndBaned, User user) {
        final Set<Chat> baned = unbanedAndBaned.getSecond();
        if (baned.isEmpty()) {
            userRepository.deleteByUserIdAndHashKey(user.getUserId(), user.getHashKey());
            return;
        }
        final Set<Chat> unbaned = unbanedAndBaned.getFirst();
        final Set<Long> unbandedGroupIds = unbaned.stream().map(Chat::getId).collect(Collectors.toSet());
        final Set<Group> newUserGroups = user.getGroups().stream()
                .filter(group -> !unbandedGroupIds.contains(group.getGroupId()))
                .collect(Collectors.toSet());
        user.setGroups(newUserGroups);
        userRepository.saveAndFlush(user);
    }

    private Pair<Set<Chat>, Set<Chat>> separateOnUnbanedAndBaned(Set<Chat> chats, Integer userId) {
        final Set<Chat> unbaned = new HashSet<>();
        final Set<Chat> baned = new HashSet<>();
        for (Chat chat : chats) {
            final boolean unban = preparedRequestService.unban(chat.getId(), userId);
            if (unban) {
                unbaned.add(chat);
            } else {
                baned.add(chat);
            }
        }
        return Pair.of(unbaned, baned);
    }

    private String makeResultMessage(Pair<Set<Chat>, Set<Chat>> unbanedAndBaned, Update update) {
        final Set<Chat> unbaned = unbanedAndBaned.getFirst();
        final Set<Chat> baned = unbanedAndBaned.getSecond();

        String result = "";
        if (!unbaned.isEmpty()) {
            result = bundleMessageSourceManager.findMessage(update, "command.invite.part.links");
            for (Chat chat : unbaned) {
                final String link = " <a href=\""
                        + chat.getInviteLink()
                        + "\">"
                        + chat.getTitle()
                        + "</a>";
                result = result.concat(link).concat(",");
            }
            result = StringUtil.replaceLastCharWithDot(result);
        }

        if (!unbaned.isEmpty() && !baned.isEmpty()) {
            result = result.concat("\n");
        }

        if (!baned.isEmpty()) {
            final String message = bundleMessageSourceManager.findMessage(update, "command.invite.part.can-not-unban");
            result = result + message;
            for (Chat chat : baned) {
                result = result.concat(" ")
                        .concat(chat.getTitle())
                        .concat(",");
            }
            result = StringUtil.replaceLastCharWithDot(result);
        }
        return result;
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.INVITE;
    }
}
