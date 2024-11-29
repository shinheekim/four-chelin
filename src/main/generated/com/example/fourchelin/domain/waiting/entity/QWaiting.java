package com.example.fourchelin.domain.waiting.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWaiting is a Querydsl query type for Waiting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWaiting extends EntityPathBase<Waiting> {

    private static final long serialVersionUID = 1780865287L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWaiting waiting = new QWaiting("waiting");

    public final com.example.fourchelin.common.baseentity.QTimestamped _super = new com.example.fourchelin.common.baseentity.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.fourchelin.domain.waiting.enums.WaitingMealType> mealType = createEnum("mealType", com.example.fourchelin.domain.waiting.enums.WaitingMealType.class);

    public final com.example.fourchelin.domain.member.entity.QMember member;

    public final NumberPath<Long> personnel = createNumber("personnel", Long.class);

    public final EnumPath<com.example.fourchelin.domain.waiting.enums.WaitingStatus> status = createEnum("status", com.example.fourchelin.domain.waiting.enums.WaitingStatus.class);

    public final com.example.fourchelin.domain.store.entity.QStore store;

    public final EnumPath<com.example.fourchelin.domain.waiting.enums.WaitingType> type = createEnum("type", com.example.fourchelin.domain.waiting.enums.WaitingType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public final NumberPath<Long> waitingNumber = createNumber("waitingNumber", Long.class);

    public QWaiting(String variable) {
        this(Waiting.class, forVariable(variable), INITS);
    }

    public QWaiting(Path<? extends Waiting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWaiting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWaiting(PathMetadata metadata, PathInits inits) {
        this(Waiting.class, metadata, inits);
    }

    public QWaiting(Class<? extends Waiting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.fourchelin.domain.member.entity.QMember(forProperty("member")) : null;
        this.store = inits.isInitialized("store") ? new com.example.fourchelin.domain.store.entity.QStore(forProperty("store")) : null;
    }

}

