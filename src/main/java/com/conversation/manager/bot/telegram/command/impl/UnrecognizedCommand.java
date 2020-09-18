package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.conversation.manager.bot.telegram.command.extractor.impl.CommandPartExtractor.FIRST_SYMBOL;

@Component
@RequiredArgsConstructor
public class UnrecognizedCommand extends AbstractBotCommand {

    private final String template = "I can't recognize the command '"
            + FIRST_SYMBOL
            + "%s"
            + "'. Could you try again?";

    private final CommandPartExtractor commandPartExtractor;

    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        final String resultMessage = getResultMessage(update);
        return new SendMessage(chatId, resultMessage);
    }

    private String getResultMessage(Update update) {
        final String text = update.getMessage().getText();
        final Optional<String> command = commandPartExtractor.extract(text);
        return String.format(template,
                command.orElseThrow(() -> new IllegalArgumentException("Null in message or text.")));
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.UNRECOGNIZED;
    }
}
