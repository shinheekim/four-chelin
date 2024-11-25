package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.search.entity.QSearchHistory;
import com.example.fourchelin.domain.search.entity.SearchHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchHistoryRepositoryCustomImpl implements SearchHistoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SearchHistory findByKeywordAndMember(String keyword, Member member) {
        QSearchHistory searchHistory = QSearchHistory.searchHistory;

        return jpaQueryFactory
                .selectFrom(searchHistory)
                .where(searchHistory.keyword.eq(keyword)
                        .and(searchHistory.member.eq(member))
                )
                .fetchOne();
    }
}
