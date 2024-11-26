package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.search.entity.PopularKeyword;

import java.time.LocalDate;
import java.util.Optional;

public interface PopularKeywordRepositoryCustom {
    Optional<PopularKeyword> findByKeywordAndTrendDate(String keyword, LocalDate trendDate);
}
