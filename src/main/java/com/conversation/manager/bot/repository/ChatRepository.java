package com.conversation.manager.bot.repository;

import com.conversation.manager.bot.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
