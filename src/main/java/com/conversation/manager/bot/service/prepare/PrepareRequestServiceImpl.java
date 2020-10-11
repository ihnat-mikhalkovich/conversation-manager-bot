package com.conversation.manager.bot.service.prepare;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.repository.GroupRepository;
import com.conversation.manager.bot.service.request.TelegramRequestService;
import com.conversation.manager.bot.telegram.method.GetChatWithEquals;
import com.conversation.manager.bot.telegram.method.SaveGetChatMember;
import com.conversation.manager.bot.telegram.method.UnbanChatMemberWithEquals;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PrepareRequestServiceImpl implements PrepareRequestService {

    private final TelegramRequestService telegramRequestService;

    private final GroupRepository groupRepository;

    @Override
    public Optional<Chat> findChat(Long groupId) {
        final GetChat getChat = new GetChatWithEquals();
        getChat.setChatId(groupId);
        return telegramRequestService.getChat(getChat);
    }

    @Override
    public Set<Group> findGroupsByUserId(Integer userId) {

        final List<Group> all = groupRepository.findAll();
        final Set<Group> groups = new HashSet<>();

        all.forEach(group -> {
            final GetChatMember getChatMember = new SaveGetChatMember();
            getChatMember.setUserId(userId);
            getChatMember.setChatId(group.getGroupId());
            final boolean exist = telegramRequestService.existChatMember(getChatMember);
            if (exist) {
                groups.add(group);
            }
        });

        return groups;
    }

    @Override
    public boolean unban(Long chatId, Integer userId) {
        final UnbanChatMember unbanChatMember = new UnbanChatMemberWithEquals();
        unbanChatMember.setChatId(chatId);
        unbanChatMember.setUserId(userId);
        return telegramRequestService.unbanChatMember(unbanChatMember);
    }
}
