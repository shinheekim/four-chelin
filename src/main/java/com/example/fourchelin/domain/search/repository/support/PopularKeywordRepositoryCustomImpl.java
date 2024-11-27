package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.search.entity.PopularKeyword;
import com.example.fourchelin.domain.search.entity.QPopularKeyword;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PopularKeywordRepositoryCustomImpl implements PopularKeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PopularKeyword> findByKeywordAndTrendDate(String keyword, LocalDate trendDate) {
        QPopularKeyword popularKeyword = QPopularKeyword.popularKeyword;

        PopularKeyword result = queryFactory
                .selectFrom(popularKeyword)
                .where(
                        popularKeyword.keyword.eq(keyword),
                        popularKeyword.trendDate.eq(trendDate)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
