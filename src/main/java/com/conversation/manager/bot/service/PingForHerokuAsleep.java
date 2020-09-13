package com.conversation.manager.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PingForHerokuAsleep {

    private final RestTemplate restTemplate;

    @Scheduled(cron = "0 0 */6 ? * *")
    public void pingToGoogle() {
        log.info("Ping to google. Start.");
        restTemplate.getForEntity("https://www.google.com/", String.class);
        log.info("Ping to google. End.");
    }
}
