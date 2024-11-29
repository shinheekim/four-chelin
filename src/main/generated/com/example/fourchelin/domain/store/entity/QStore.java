package com.example.fourchelin.domain.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = 333056263L;

    public static final QStore store = new QStore("store");

    public final com.example.fourchelin.common.baseentity.QTimestamped _super = new com.example.fourchelin.common.baseentity.QTimestamped(this);

    public final StringPath address = createString("address");

    public final EnumPath<com.example.fourchelin.domain.store.enums.StoreCategory> category = createEnum("category", com.example.fourchelin.domain.store.enums.StoreCategory.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> star = createNumber("star", Integer.class);

    public final EnumPath<com.example.fourchelin.domain.store.enums.StoreStatus> status = createEnum("status", com.example.fourchelin.domain.store.enums.StoreStatus.class);

    public final StringPath storeName = createString("storeName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QStore(String variable) {
        super(Store.class, forVariable(variable));
    }

    public QStore(Path<? extends Store> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStore(PathMetadata metadata) {
        super(Store.class, metadata);
    }

}

