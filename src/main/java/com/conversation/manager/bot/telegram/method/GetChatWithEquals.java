package com.conversation.manager.bot.telegram.method;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;

import java.util.Objects;

public class GetChatWithEquals extends GetChat {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final GetChat getChat = (GetChat) o;
        return Objects.equals(this.getChatId(), getChat.getChatId());
    }
}
