package com.conversation.manager.bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.TABLE;

@Entity
@Table(name = "TELEGRAM_GROUP")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
public class Group {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    @JsonIgnore
    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();
}