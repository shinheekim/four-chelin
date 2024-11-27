package com.example.fourchelin.domain.search.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularKeywordCache {

    private String keyword;
    private LocalDate date;
    private Long count;

    public PopularKeywordCache(String keyword, LocalDate date, Long count) {
        this.keyword = keyword;
        this.date = date;
        this.count = count;
    }

    public void incrementCount() {
        this.count++;
    }
}
