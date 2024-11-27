package com.example.fourchelin.domain.waiting.dto.response;

import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record WaitingInfoResponse(
        WaitingResponse information,
        StoreCategory storeCategory,
        String storeLocation,
        LocalDate createAt
) {
    public static WaitingInfoResponse from(Waiting waiting) {
        return WaitingInfoResponse.builder()
                .information(WaitingResponse.from(waiting))
                .storeCategory(waiting.getStore().getCategory())
                .storeLocation(waiting.getStore().getAddress())
                .createAt(waiting.getCreateAt().toLocalDate())
                .build();
    }
}
