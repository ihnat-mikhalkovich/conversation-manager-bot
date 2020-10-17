package com.conversation.manager.bot.service.request;

import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.Optional;

public interface TelegramRequestService {
    boolean existChatMember(GetChatMember getChatMember);

    Optional<Chat> getChat(GetChat getChat);

    boolean unbanChatMember(UnbanChatMember unbanChatMember);

    String exposeLink(ExportChatInviteLink exportChatInviteLink);

    boolean kickFromChat(KickChatMember kickChatMember);
}
