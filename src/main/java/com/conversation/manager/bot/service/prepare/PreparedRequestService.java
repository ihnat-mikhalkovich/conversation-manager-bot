package com.conversation.manager.bot.service.prepare;

import com.conversation.manager.bot.entity.Group;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface PreparedRequestService {

    default Set<Chat> findChats(Set<Group> groups) {
        return groups.stream()
                .map(this::findChat)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    default Optional<Chat> findChat(Group group) {
        return this.findChat(group.getGroupId());
    }

    Optional<Chat> findChat(Long groupId);

    Set<Group> findGroupsByUserId(Integer userId);

    boolean unban(Long chatId, Integer userId);

    boolean kickFromChat(Long chatId, Integer userId);
}
