package com.conversation.manager.bot.service.prepare;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.repository.GroupRepository;
import com.conversation.manager.bot.service.request.TelegramRequestService;
import com.conversation.manager.bot.telegram.method.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PreparedRequestServiceImpl implements PreparedRequestService {

    private final TelegramRequestService telegramRequestService;

    private final GroupRepository groupRepository;

    @Override
    public Optional<Chat> findChat(Long groupId) {
        final GetChat getChat = new GetChatWithEquals();
        getChat.setChatId(groupId);
        Optional<Chat> optChat = telegramRequestService.getChat(getChat);

        if (!optChat.isPresent()) {
            return optChat;
        }

        final Chat chat = optChat.get();
        if (StringUtils.isEmpty(chat.getInviteLink())) {
            final ExportChatInviteLink exportChatInviteLink = new ExportChatInviteLinkWithEquals();
            exportChatInviteLink.setChatId(getChat.getChatId());
            final String link = telegramRequestService.exposeLink(exportChatInviteLink);
            if (StringUtils.isEmpty(link)) {
                return Optional.empty();
            }
            optChat = telegramRequestService.getChat(getChat);
        }

        return optChat;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Group> findGroupsByUserId(Integer userId) {
        Pageable pageRequest = PageRequest.of(0, 1);
        Page<Group> onePage = groupRepository.findAll(pageRequest);

        final Set<Group> groups = new HashSet<>();

        while (!onePage.isEmpty()) {
            pageRequest = pageRequest.next();

            onePage.forEach(group -> {
                final GetChatMember getChatMember = new SaveGetChatMember();
                getChatMember.setUserId(userId);
                getChatMember.setChatId(group.getGroupId());
                final boolean exist = telegramRequestService.existChatMember(getChatMember);
                if (exist) {
                    groups.add(group);
                }
            });
            onePage = groupRepository.findAll(pageRequest);
        }
        return groups;
    }

    @Override
    public boolean unban(Long chatId, Integer userId) {
        final UnbanChatMember unbanChatMember = new UnbanChatMemberWithEquals();
        unbanChatMember.setChatId(chatId);
        unbanChatMember.setUserId(userId);
        return telegramRequestService.unbanChatMember(unbanChatMember);
    }

    @Override
    public boolean kickFromChat(Long chatId, Integer userId) {
        final KickChatMember kickChatMember = new KickChatMemberWithEquals();
        kickChatMember.setChatId(chatId);
        kickChatMember.setUserId(userId);
        return telegramRequestService.kickFromChat(kickChatMember);
    }

    @Override
    public boolean leaveChat(Long chatId) {
        final LeaveChat leaveChat = new LeaveChat();
        leaveChat.setChatId(chatId);
        return telegramRequestService.leave(leaveChat);
    }

    @Override
    public Optional<Message> sendMessage(Long chatId, String text) {
        final SendMessage sendMessage = new SendMessage(chatId, text);
        return telegramRequestService.sendMessage(sendMessage);
    }
}
