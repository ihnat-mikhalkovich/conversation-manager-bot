package com.conversation.manager.bot.telegram.method;

import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;

import java.util.Objects;

public class ExportChatInviteLinkWithEquals extends ExportChatInviteLink {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final ExportChatInviteLink exportChatInviteLink = (ExportChatInviteLink) o;
        return Objects.equals(this.getChatId(), exportChatInviteLink.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getChatId());
    }
}
