package com.conversation.manager.bot.controller;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.entity.User;
import com.conversation.manager.bot.repository.GroupRepository;
import com.conversation.manager.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    @RestController("/user")
    @RequiredArgsConstructor
    public static class UserController {

        private final UserRepository userRepository;

        @GetMapping("/getAllUsers")
        public ResponseEntity<List<User>> getAllUsers() {
            final List<User> all = userRepository.findAll();
            return ResponseEntity.ok(all);
        }
    }

    @RestController("/group")
    @RequiredArgsConstructor
    public static class GroupController {
        private final GroupRepository groupRepository;

        @GetMapping("/getAllGroups")
        public ResponseEntity<List<Group>> getAllGroups() {
            final List<Group> all = groupRepository.findAll();
            return ResponseEntity.ok(all);
        }
    }
}
