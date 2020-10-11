package com.conversation.manager.bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TELEGRAM_GROUP")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
public class Group {

    @Id
    @GeneratedValue
    @Column(name = "group_id")
    private Long groupId;

    @JsonIgnore
    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();
}