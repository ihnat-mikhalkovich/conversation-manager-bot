package com.conversation.manager.bot.repository;

import com.conversation.manager.bot.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testGetById() {
        final Optional<User> byId = userRepository.findById(702696623);
        System.out.println("one: " + byId.get());
    }

}