package com.example.fourchelin.domain.search.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String keyword;
    @Column(nullable = false)
    private Long searchCount;
    @Column(nullable = false)
    private LocalDate trendDate;

    public PopularKeyword(String keyword, LocalDate trendDate) {
        this.keyword = keyword;
        this.trendDate = trendDate;
        this.searchCount = 1L;
    }

    public void incrementCount() {
        this.searchCount++;
    }
}
