package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.search.entity.QSearchHistory;
import com.example.fourchelin.domain.search.entity.SearchHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchHistoryRepositoryCustomImpl implements SearchHistoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QSearchHistory searchHistory = QSearchHistory.searchHistory;

    @Override
    public SearchHistory findByKeywordAndMember(String keyword, Member member) {
        return jpaQueryFactory
                .selectFrom(searchHistory)
                .where(searchHistory.keyword.eq(keyword)
                        .and(searchHistory.member.eq(member))
                )
                .fetchOne();
    }
    @Override
    public List<SearchHistory> keywordFindByMember(Member member) {
        return jpaQueryFactory
                .selectFrom(searchHistory)
                .where(searchHistory.member.eq(member))
                .orderBy(searchHistory.searchDateTime.desc())
                .limit(10)
                .fetch();
    }
}
