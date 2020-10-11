package com.conversation.manager.bot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TELEGRAM_USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "groups")
@ToString(exclude = "groups")
public class User {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "hash_key")
    private String hashKey;

    @ManyToMany
    @JoinTable(name = "user_group",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "group_id", referencedColumnName = "group_id"))
    private Set<Group> groups = new HashSet<>();
}
