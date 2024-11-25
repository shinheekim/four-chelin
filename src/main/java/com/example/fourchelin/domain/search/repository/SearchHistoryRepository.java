package com.example.fourchelin.domain.search.repository;

import com.example.fourchelin.domain.search.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
}
