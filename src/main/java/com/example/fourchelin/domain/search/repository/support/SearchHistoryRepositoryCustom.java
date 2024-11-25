package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.search.entity.SearchHistory;

public interface SearchHistoryRepositoryCustom {
    SearchHistory findByKeywordAndMember(String keyword, Member member);
}
