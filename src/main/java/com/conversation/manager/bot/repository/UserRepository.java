package com.conversation.manager.bot.repository;

import com.conversation.manager.bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
