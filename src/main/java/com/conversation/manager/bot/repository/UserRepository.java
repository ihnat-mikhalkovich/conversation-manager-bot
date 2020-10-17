package com.conversation.manager.bot.repository;

import com.conversation.manager.bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdAndHashKey(Integer userId, String hashKey);
    Long deleteByUserIdAndHashKey(Integer userId, String hashKey);
}
