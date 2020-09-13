package com.conversation.manager.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class AgentController {

    private final TelegramWebhookBot telegramBotService;

    @PostMapping("/ask")
    public BotApiMethod<?> ask(@RequestBody Update update) {
        return telegramBotService.onWebhookUpdateReceived(update);
    }
}
