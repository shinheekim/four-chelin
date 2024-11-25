package com.example.fourchelin.domain.search.dto.response;

import com.example.fourchelin.domain.store.entity.Store;

public record SearchResponse(
        Long id,
        String storeName,
        String address
) {
    public static SearchResponse fromStoreEntity(Store store) {
        return new SearchResponse(
                store.getId(),
                store.getStoreName(),
                store.getAddress()
        );
    }
}
