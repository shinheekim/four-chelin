package com.example.fourchelin.domain.waiting.dto.response;

import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import lombok.Builder;

@Builder
public record WaitingResponse(
        String storeName,
        WaitingMealType mealType,
        long personnel,
        WaitingType waitingType,
        long waitingNumber,
        WaitingStatus waitingStatus
) {
    public static WaitingResponse from(Waiting waiting) {
        return WaitingResponse.builder()
                .storeName(waiting.getStore().getStoreName())
                .mealType(waiting.getMealType())
                .personnel(waiting.getPersonnel())
                .waitingType(waiting.getType())
                .waitingNumber(waiting.getWaitingNumber())
                .waitingStatus(waiting.getStatus())
                .build();
    }
}
