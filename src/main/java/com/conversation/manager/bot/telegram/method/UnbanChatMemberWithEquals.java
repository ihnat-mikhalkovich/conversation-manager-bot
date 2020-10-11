package com.conversation.manager.bot.telegram.method;

import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;

import java.util.Objects;

public class UnbanChatMemberWithEquals extends UnbanChatMember {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final UnbanChatMember unbanChatMember = (UnbanChatMember) o;
        return Objects.equals(this.getUserId(), unbanChatMember.getUserId())
                && Objects.equals(this.getChatId(), unbanChatMember.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getChatId(), this.getUserId());
    }
}
