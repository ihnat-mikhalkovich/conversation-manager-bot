package com.conversation.manager.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final TelegramWebhookBot telegramWebhookBot;

    @GetMapping("/username")
    public ResponseEntity<String> getUsername() {
        return ResponseEntity.ok(telegramWebhookBot.getBotUsername());
    }

    @GetMapping("/bot-path")
    public ResponseEntity<String> getBotPath() {
        return ResponseEntity.ok(telegramWebhookBot.getBotPath());
    }
}
