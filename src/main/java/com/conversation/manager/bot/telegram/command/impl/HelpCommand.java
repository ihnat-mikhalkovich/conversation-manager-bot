package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand extends AbstractBotCommand {

    private static final String MESSAGE = "/help - info about commands \n" +
            "/start - just greeting \n" +
            "/remove - the bot will kick you from all 'supergroups' where the bot is administrator\n" +
            "/invite - it will work only after '/remove'. The bot will provide you invite links for every 'supergroup' where you was removed";

    @Override
    public SendMessage process(Long chatId, Update update) {
        return new SendMessage(chatId, MESSAGE);
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.HELP;
    }
}
