package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.search.entity.PopularKeyword;
import com.example.fourchelin.domain.search.entity.QPopularKeyword;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PopularKeywordRepositoryCustomImpl implements PopularKeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPopularKeyword popularKeyword = QPopularKeyword.popularKeyword;

    @Override
    public Optional<PopularKeyword> findByKeywordAndTrendDate(String keyword, LocalDate trendDate) {
        PopularKeyword result = queryFactory
                .selectFrom(popularKeyword)
                .where(
                        popularKeyword.keyword.eq(keyword),
                        popularKeyword.trendDate.eq(trendDate)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }
    @Override
    public List<String> findTop10PopularKeywords(LocalDate fromDate) {
        return queryFactory.select(popularKeyword.keyword)
                .from(popularKeyword)
                .where(popularKeyword.trendDate.goe(fromDate))
                .orderBy(popularKeyword.searchCount.desc())
                .limit(10)
                .fetch();
    }
    @Override
    public List<PopularKeyword> findByTrendDateBetween(LocalDate startDate, LocalDate endDate) {
        QPopularKeyword popularKeyword = QPopularKeyword.popularKeyword;
        return queryFactory.selectFrom(popularKeyword)
                .where(popularKeyword.trendDate.between(startDate, endDate))
                .fetch();
    }
}
