package com.conversation.manager.bot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
public class RedirectToSwaggerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void redirectToSwaggerTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("/swagger-ui.html"));
    }
}