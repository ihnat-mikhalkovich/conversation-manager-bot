package com.conversation.manager.bot.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AgentControllerTest {

    private static final String BOT_ASK = "/bot/ask";

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private TelegramWebhookBot telegramWebhookBot;

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
