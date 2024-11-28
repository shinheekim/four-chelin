package com.example.fourchelin.domain.search.repository.support;

import com.example.fourchelin.domain.search.entity.PopularKeyword;
import com.example.fourchelin.domain.search.entity.QPopularKeyword;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.querydsl.core.Tuple;

@Repository
@RequiredArgsConstructor
public class PopularKeywordRepositoryCustomImpl implements PopularKeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
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
    public void saveOrUpdateKeyword(String keyword, Long count, LocalDate today) {
        PopularKeyword existingKeyword = queryFactory
                .selectFrom(QPopularKeyword.popularKeyword)
                .where(
                        QPopularKeyword.popularKeyword.keyword.eq(keyword),
                        QPopularKeyword.popularKeyword.trendDate.eq(today)
                )
                .fetchOne();
        if (existingKeyword != null) {
            existingKeyword.incrementCount(count);
        } else {
            PopularKeyword newKeyword = new PopularKeyword(keyword, today);
            newKeyword.incrementCount(count);
            entityManager.persist(newKeyword);
        }
    }

    @Override
    public Map<String, Long> findTop10KeywordsForLast7Days() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<Tuple> results = queryFactory
                .select(popularKeyword.keyword, popularKeyword.searchCount.sum())
                .from(popularKeyword)
                .where(popularKeyword.trendDate.goe(sevenDaysAgo))
                .groupBy(popularKeyword.keyword)
                .orderBy(popularKeyword.searchCount.sum().desc())
                .limit(10)
                .fetch();
        Map<String, Long> topKeywords = new HashMap<>();
        for (Tuple tuple : results) {
            topKeywords.put(tuple.get(popularKeyword.keyword), tuple.get(popularKeyword.searchCount.sum()));
        }
        return topKeywords;
    }
}
