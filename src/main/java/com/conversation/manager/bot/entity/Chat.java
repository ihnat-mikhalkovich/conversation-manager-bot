package com.conversation.manager.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TELEGRAM_CHAT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "userId")
    private Long userId;
}