package com.example.fourchelin.domain.search.entity;

import com.example.fourchelin.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String keyword;
    @Column(nullable = false)
    private LocalDateTime searchDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public SearchHistory(String keyword, LocalDateTime searchDateTime) {
        this.keyword = keyword;
        this.searchDateTime = searchDateTime;
    }
}
