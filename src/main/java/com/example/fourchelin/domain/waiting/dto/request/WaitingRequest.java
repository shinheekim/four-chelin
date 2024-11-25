package com.example.fourchelin.domain.waiting.dto.request;

import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import jakarta.validation.constraints.NotBlank;

public record WaitingRequest(
        @NotBlank
        WaitingType waitingType,
        @NotBlank
        WaitingMealType mealType,
        long personnel,
        Long storeId
) {
}
