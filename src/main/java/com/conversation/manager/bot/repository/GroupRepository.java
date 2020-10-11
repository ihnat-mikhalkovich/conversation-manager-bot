package com.conversation.manager.bot.repository;

import com.conversation.manager.bot.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
