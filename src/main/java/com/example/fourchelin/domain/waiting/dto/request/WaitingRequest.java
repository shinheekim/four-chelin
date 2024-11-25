package com.example.fourchelin.domain.waiting.dto.request;

import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WaitingRequest(
        @NotNull
        WaitingType waitingType,
        @NotNull
        WaitingMealType mealType,
        @Min(1)
        long personnel,
        @NotNull
        Long storeId
) { }
