package com.conversation.manager.bot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.TABLE;

@Entity
@Table(name = "TELEGRAM_USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "groups")
@ToString(exclude = "groups")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "hash_key")
    private String hashKey;

    @ManyToMany
    @JoinTable(name = "user_group",
            joinColumns =
            @JoinColumn(name = "telegram_user_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "telegram_group_id", referencedColumnName = "id"))
    private Set<Group> groups = new HashSet<>();
}
