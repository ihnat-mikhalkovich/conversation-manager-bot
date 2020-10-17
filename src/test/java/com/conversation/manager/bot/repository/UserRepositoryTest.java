package com.conversation.manager.bot.repository;

import com.conversation.manager.bot.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testGetById() {
        final Optional<User> byId = userRepository.findByUserIdAndHashKey(702696623, "ee10c315eba2c75b403ea99136f5b48d");
        System.out.println("one: " + byId.get());
    }

}