package com.conversation.manager.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TELEGRAM_USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "telegram_username")
    private String username;

    @Column(name = "hashed_key")
    private String hashedKey;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userId")
//    @JoinColumn(name = "chat_id")
    private Set<Chat> chats;
}
