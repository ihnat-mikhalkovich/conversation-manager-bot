package com.conversation.manager.bot.telegram.method;

import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.Objects;

public class ChatWithEquals extends Chat {

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getInviteLink());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final ChatWithEquals chatWithEquals = (ChatWithEquals) o;
        return Objects.equals(this.getId(), chatWithEquals.getId())
                && Objects.equals(this.getInviteLink(), chatWithEquals.getInviteLink());
    }
}
