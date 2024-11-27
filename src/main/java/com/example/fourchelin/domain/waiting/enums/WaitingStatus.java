package com.example.fourchelin.domain.waiting.enums;

import lombok.Getter;

@Getter
public enum WaitingStatus { // 대기 상태(대기중, 취소, 완료)
    WAITING("waiting"),
    CANCEL("canceled"),
    COMPLETE("completed");

    private final String status;

    WaitingStatus(String status) {
        this.status = status;
    }

    public static WaitingStatus fromStatus(String status) {
        for (WaitingStatus value : WaitingStatus.values()) {
            if (value.getStatus().equalsIgnoreCase(status))
                return value;
        }
        throw new IllegalArgumentException("해당하는 대기 상태가 없습니다.");
    }
}
