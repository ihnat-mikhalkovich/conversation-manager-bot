package com.conversation.manager.bot.service.prepare;

import com.conversation.manager.bot.entity.Group;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface PreparedRequestService {

    default Set<Chat> findChats(Set<Group> groups) {
        return groups.stream()
                .map(this::findChats)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    default Optional<Chat> findChats(Group group) {
        return this.findChats(group.getGroupId());
    }

    Optional<Chat> findChats(Long groupId);

    Set<Group> findGroupsByUserId(Integer userId);

    boolean unban(Long chatId, Integer userId);
}
