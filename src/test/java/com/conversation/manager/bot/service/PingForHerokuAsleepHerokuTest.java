package com.conversation.manager.bot.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "CLOUD_PLATFORM=heroku",
        "cloud.heroku.ping-google-cron=* * * ? * *"
})
public class PingForHerokuAsleepHerokuTest {

    @SpyBean
    private PingForHerokuAsleep pingForHerokuAsleep;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUpRestTemplate() {
        when(restTemplate.getForEntity("https://www.google.com/", String.class))
                .thenReturn(ResponseEntity.ok("Ok google."));
    }

    @Test
    public void testSchedule() {
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(pingForHerokuAsleep, times(5)).pingToGoogle());
    }

    @Test
    public void checkPingToGoogle() {
        pingForHerokuAsleep.pingToGoogle();
        pingForHerokuAsleep.pingToGoogle();
        verify(restTemplate, times(2)).getForEntity("https://www.google.com/", String.class);
    }

    @SpringBootTest(properties = "CLOUD_PLATFORM=local")
    public static class NotInHeroku {

        @Autowired
        private ConfigurableApplicationContext applicationContext;

        @Test
        public void isCreated() {
            assertThrows(NoSuchBeanDefinitionException.class, () -> {
                final PingForHerokuAsleep bean
                        = applicationContext.getBean(PingForHerokuAsleep.class);
            });
        }
    }

    @SpringBootTest(properties = "CLOUD_PLATFORM=heroku")
    public static class InHeroku {

        @Autowired
        private ConfigurableApplicationContext applicationContext;

        @Test
        public void isCreated() {
            final PingForHerokuAsleep bean
                    = applicationContext.getBean(PingForHerokuAsleep.class);

            assertNotNull(bean);
        }
    }
}