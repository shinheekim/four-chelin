package com.example.fourchelin.domain.search.repository;

import com.example.fourchelin.domain.search.entity.PopularKeyword;
import com.example.fourchelin.domain.search.repository.support.PopularKeywordRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularKeywordRepository extends JpaRepository<PopularKeyword, Long>, PopularKeywordRepositoryCustom {
}
