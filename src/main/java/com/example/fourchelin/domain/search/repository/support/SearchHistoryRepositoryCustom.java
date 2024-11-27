package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.search.entity.SearchHistory;

import java.util.List;

public interface SearchHistoryRepositoryCustom {
    SearchHistory findByKeywordAndMember(String keyword, Member member);
    List<SearchHistory> keywordFindByMember(Member member);
}
