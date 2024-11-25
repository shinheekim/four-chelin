package com.example.fourchelin.domain.member.entity;

import com.example.fourchelin.common.baseentity.Timestamped;
import com.example.fourchelin.domain.member.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public Member(String phone, String nickname, String password, MemberRole role) {
        this.phone = phone;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public static Member createMember(String phone, String nickname, String password, MemberRole role) {
        return new Member(phone, nickname, password, role);
    }
}
