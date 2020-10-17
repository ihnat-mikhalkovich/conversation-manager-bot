package com.conversation.manager.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@RestController
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/set-webhook")
    public ResponseEntity<String> setWebhook(@RequestBody final String url) throws TelegramApiRequestException {
        final String endpoint;

        if (url.endsWith("/")) {
            endpoint = url + "bot/ask";
        } else {
            endpoint = url + "/bot/ask";
        }

        telegramWebhookBot.setWebhook(endpoint, null);
        log.info("Set webhook to url: {}", endpoint);
        return ResponseEntity.ok(endpoint);
    }
}
