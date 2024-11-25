package com.example.fourchelin.domain.store.dto.request;

import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.enums.StoreStatus;

public record StoreRequest(
        String storeName,
        StoreStatus status,
        StoreCategory category,
        String address,
        Integer star) {
}
