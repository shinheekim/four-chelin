package com.example.fourchelin.domain.search.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSearchHistory is a Querydsl query type for SearchHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchHistory extends EntityPathBase<SearchHistory> {

    private static final long serialVersionUID = -1495750205L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSearchHistory searchHistory = new QSearchHistory("searchHistory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public final com.example.fourchelin.domain.member.entity.QMember member;

    public final DateTimePath<java.time.LocalDateTime> searchDateTime = createDateTime("searchDateTime", java.time.LocalDateTime.class);

    public QSearchHistory(String variable) {
        this(SearchHistory.class, forVariable(variable), INITS);
    }

    public QSearchHistory(Path<? extends SearchHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSearchHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSearchHistory(PathMetadata metadata, PathInits inits) {
        this(SearchHistory.class, metadata, inits);
    }

    public QSearchHistory(Class<? extends SearchHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.fourchelin.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

