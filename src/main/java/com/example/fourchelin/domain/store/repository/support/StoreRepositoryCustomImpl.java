package com.example.fourchelin.domain.store.repository.support;

import com.example.fourchelin.domain.store.entity.QStore;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreRepositoryCustomImpl extends QuerydslRepositorySupport implements StoreRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    public StoreRepositoryCustomImpl() {
        super(Store.class);
    }

    QStore store = QStore.store;

    @Override
    public Page<Store> findByConditionStores(String keyword, String category, Integer star, Pageable pageable) {

        JPAQuery<Store> query = jpaQueryFactory
                .selectFrom(store)
                .where(eqCategory(category), eqStar(star), containName(keyword));

        List<Store> stores = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<Store>(stores, pageable, query.fetchCount());

    }

    private BooleanExpression eqCategory(String category) {
        if (category == null) {
            return null;
        }
        return store.category.eq(StoreCategory.valueOf(category));
    }

    private BooleanExpression eqStar(Integer star) {
        if (star == null) {
            return null;
        }
        return store.star.eq(star);
    }

    private BooleanExpression containName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return store.storeName.containsIgnoreCase(keyword);

    }
}
