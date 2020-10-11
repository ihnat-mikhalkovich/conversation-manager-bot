package com.conversation.manager.bot.telegram.method;

import com.fasterxml.jackson.core.type.TypeReference;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.Objects;

public class SaveGetChatMember extends GetChatMember {

    @Override
    public ChatMember deserializeResponse(String answer) throws TelegramApiRequestException {
        try {
            ApiResponse<ChatMember> result = OBJECT_MAPPER.readValue(answer,
                    new TypeReference<ApiResponse<ChatMember>>(){});
            if (result.getOk()) {
                return result.getResult();
            } else {
                return new ChatMember();
            }
        } catch (IOException e) {
            throw new TelegramApiRequestException("Unable to deserialize response", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final SaveGetChatMember saveGetChatMember = (SaveGetChatMember) o;
        return Objects.equals(this.getUserId(), saveGetChatMember.getUserId())
                && Objects.equals(this.getChatId(), saveGetChatMember.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getChatId(), this.getUserId());
    }
}
