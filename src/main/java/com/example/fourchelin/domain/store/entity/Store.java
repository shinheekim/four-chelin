package com.example.fourchelin.domain.store.entity;

import com.example.fourchelin.common.baseentity.Timestamped;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.enums.StoreStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Min(0)
    @Max(4)
    @Column
    private int star;

    @Column
    private LocalDateTime monitoringDate;

    @Column
    private String keyword;


    public Store(Long id, String storeName, String address, int star, StoreStatus status, LocalDateTime monitoringDate) {
        this.id = id;
        this.storeName = storeName;
        this.status = status;
        this.address = address;
        this.star = star;
        this.monitoringDate = monitoringDate;
    }
}
