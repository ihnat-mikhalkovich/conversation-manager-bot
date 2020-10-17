package com.conversation.manager.bot.service.request;

import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

public interface TelegramRequestService {
    boolean existChatMember(GetChatMember getChatMember);

    Optional<Chat> getChat(GetChat getChat);

    boolean unbanChatMember(UnbanChatMember unbanChatMember);

    String exposeLink(ExportChatInviteLink exportChatInviteLink);

    boolean kickFromChat(KickChatMember kickChatMember);

    boolean leave(LeaveChat leaveChat);

    Optional<Message> sendMessage(SendMessage sendMessage);
}
