package com.conversation.manager.bot.service;

import com.conversation.manager.bot.telegram.command.BotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandDirector;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import com.conversation.manager.bot.telegram.command.recognizer.CommandRecognizer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class TelegramBotService extends TelegramWebhookBot {

    private final BotCommandDirector botCommandDirector;
    private final CommandRecognizer commandRecognizer;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.path}")
    private String botPath;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        final BotCommandType commandType = commandRecognizer.recognize(update);
        final BotCommand command = botCommandDirector.getCommand(commandType);
        return command.execute(update);
    }
}
