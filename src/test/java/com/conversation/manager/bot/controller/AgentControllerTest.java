package com.conversation.manager.bot.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ReflectionUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AgentControllerTest {

    private static final String BOT_ASK = "/bot/ask";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelegramWebhookBot telegramWebhookBot;

    @Test
    public void testUri() throws Exception {
//        final Update update = createUpdate();
//        when(telegramWebhookBot.onWebhookUpdateReceived(update))
//                .thenReturn();
//
//        mockMvc.perform(post(BOT_ASK).content(update))
//                .andExpect(status().isOk())
//                .andExpect(content().string(contains("Hi")));
    }

    private Update createUpdate() {
        final Update update = Mockito.mock(Update.class);
        final Message message = Mockito.mock(Message.class);
        when(message.getText()).thenReturn("Hello world");
        when(update.getMessage()).thenReturn(message);
        return update;
    }
}
