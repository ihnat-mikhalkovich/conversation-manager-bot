package com.conversation.manager.bot.controller;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        final List<User> all = userRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/getUserChats")
    @Transactional
    public ResponseEntity<Set<Group>> getUserChats() {
        final List<User> all = userRepository.findAll();
        final User next = all.iterator().next();
        return ResponseEntity.ok(next.getGroups());
    }
}
