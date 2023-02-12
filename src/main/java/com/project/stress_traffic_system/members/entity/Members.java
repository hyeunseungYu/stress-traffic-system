package com.project.stress_traffic_system.members.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "USERS")
@Entity
@Getter
@NoArgsConstructor
public class Members implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MembersRoleEnum role;

    public Members(String username, String password, MembersRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
