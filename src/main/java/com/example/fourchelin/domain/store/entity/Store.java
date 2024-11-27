package com.example.fourchelin.domain.store.entity;

import com.example.fourchelin.common.baseentity.Timestamped;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Column(nullable = false)
    private String address;

    @Column
    private int star;

    public Store(Long id, String storeName, String address) {
        this.id = id;
        this.storeName = storeName;
        this.address = address;
    }
}
