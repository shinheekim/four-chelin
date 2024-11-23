package com.example.fourchelin.domain.member.entity;

import com.example.fourchelin.common.baseentity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    public Member(String phone, String nickname, String password) {
        this.phone = phone;
        this.nickname = nickname;
        this.password = password;
    }

    public static Member of(String phone, String nickname, String password) {
        return new Member(phone, nickname, password);
    }
}
