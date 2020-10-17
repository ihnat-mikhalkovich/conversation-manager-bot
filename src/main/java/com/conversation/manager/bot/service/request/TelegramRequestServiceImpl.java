package com.conversation.manager.bot.service.request;

import com.conversation.manager.bot.service.TelegramBotService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;
import java.util.Optional;

@Service
@Getter
public class TelegramRequestServiceImpl implements TelegramRequestService {

    private TelegramBotService telegramBotService;

    @Autowired
    @Qualifier("telegramBotService")
    public void setTelegramBotService(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @Override
    public boolean existChatMember(GetChatMember getChatMember) {
        try {
            final ChatMember member = telegramBotService.execute(getChatMember);
            if (Objects.isNull(member.getUser())) {
                return false;
            }
            final boolean isLeft = "left".equals(member.getStatus());
            final boolean isKicked = "kicked".equals(member.getStatus());
            return !isLeft && !isKicked;
        } catch (TelegramApiException e) {
            return false;
        }
    }

    @Override
    public Optional<Chat> getChat(GetChat getChat) {
        try {
            final Chat chat = telegramBotService.execute(getChat);
            return Optional.of(chat);
        } catch (TelegramApiException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean unbanChatMember(UnbanChatMember unbanChatMember) {
        try {
            return telegramBotService.execute(unbanChatMember);
        } catch (TelegramApiException e) {
            return false;
        }
    }

    @Override
    public String exposeLink(ExportChatInviteLink exportChatInviteLink) {
        try {
            return telegramBotService.execute(exportChatInviteLink);
        } catch (TelegramApiException e) {
            return "";
        }
    }

    @Override
    public boolean kickFromChat(KickChatMember kickChatMember) {
        try {
            return telegramBotService.execute(kickChatMember);
        } catch (TelegramApiException e) {
            return false;
        }
    }
}
