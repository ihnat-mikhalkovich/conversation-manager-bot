package com.conversation.manager.bot.telegram.command.recognizer;

import com.conversation.manager.bot.telegram.command.BotCommandType;
import com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractCommandRecognizer implements CommandRecognizer {

    protected final CommandPartExtractor commandPartExtractor;

    @Value("${telegram.bot.username}")
    @Setter
    @Getter
    private String botUsername;

    @Override
    public BotCommandType recognize(Update update) {
        if (!update.hasMessage()) {
            return BotCommandType.DO_NOTHING;
        }

        final Message message = update.getMessage();
        final Boolean isSupergroup = Optional.ofNullable(message.getChat().isSuperGroupChat()).orElse(false);
        final Boolean isUserChat = Optional.ofNullable(message.getChat().isUserChat()).orElse(false);
        if (!isSupergroup && !isUserChat) {
            return BotCommandType.CHAT_IS_NOT_SUPERGROUP;
        }

        final List<User> newChatMembers = message.getNewChatMembers();
        final Optional<User> optBot = newChatMembers.stream()
                .filter(user -> user.getBot()
                        && botUsername.equals(user.getUserName()))
                .findFirst();

        if (optBot.isPresent()) {
            return BotCommandType.BOT_ADDED_TO_SUPERGROUP;
        }

        if (!message.hasText() || !message.getChat().isUserChat()) {
            return BotCommandType.DO_NOTHING;
        }

        return this.recognize(message);
    }

    public BotCommandType recognize(Message message) {
        if (!message.hasText()) {
            return BotCommandType.UNRECOGNIZED;
        }
        final String text = message.getText();
        return this.recognize(text);
    }

    public BotCommandType recognize(String text) {
        return this.process(text);
    }

    protected abstract BotCommandType process(String text);
}
