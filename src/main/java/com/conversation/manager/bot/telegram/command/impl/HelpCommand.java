package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand extends AbstractBotCommand {

    @Override
    public SendMessage process(Long chatId, Update update) {
        final String message = bundleMessageSourceManager.findMessage(update, "command.help");
        return new SendMessage(chatId, message);
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.HELP;
    }
}
