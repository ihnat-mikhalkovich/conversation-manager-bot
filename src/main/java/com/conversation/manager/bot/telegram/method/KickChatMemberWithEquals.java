package com.conversation.manager.bot.telegram.method;

import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;

import java.util.Objects;

public class KickChatMemberWithEquals extends KickChatMember {
    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getChatId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final KickChatMember kickChatMember = (KickChatMember) o;
        return Objects.equals(this.getUserId(), kickChatMember.getUserId())
                && Objects.equals(this.getChatId(), kickChatMember.getChatId());
    }
}
