package com.conversation.manager.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BotConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
