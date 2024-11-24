package com.example.fourchelin.domain.store.dto.response;

import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.enums.StoreStatus;

public record StorePageResponse(
        Long id,
        String storeName,
        StoreStatus status,
        StoreCategory category,
        String address,
        Integer star
) {
}
