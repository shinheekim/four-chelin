package com.example.fourchelin.domain.store.dto.response;

import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.enums.StoreStatus;

public record StoreResponse(
        Long id,
        String storeName,
        StoreStatus status,
        StoreCategory category,
        String address,
        Integer star
) {
    public static StoreResponse to(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getStoreName(),
                store.getStatus(),
                store.getCategory(),
                store.getAddress(),
                store.getStar()
        );

    }
}
