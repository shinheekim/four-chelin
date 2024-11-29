package com.example.fourchelin.domain.search.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPopularKeyword is a Querydsl query type for PopularKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPopularKeyword extends EntityPathBase<PopularKeyword> {

    private static final long serialVersionUID = 1525337017L;

    public static final QPopularKeyword popularKeyword = new QPopularKeyword("popularKeyword");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public final NumberPath<Long> searchCount = createNumber("searchCount", Long.class);

    public final DatePath<java.time.LocalDate> trendDate = createDate("trendDate", java.time.LocalDate.class);

    public QPopularKeyword(String variable) {
        super(PopularKeyword.class, forVariable(variable));
    }

    public QPopularKeyword(Path<? extends PopularKeyword> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPopularKeyword(PathMetadata metadata) {
        super(PopularKeyword.class, metadata);
    }

}

