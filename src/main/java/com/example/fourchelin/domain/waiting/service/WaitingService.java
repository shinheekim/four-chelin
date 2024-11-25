package com.example.fourchelin.domain.waiting.service;

import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.dto.response.WaitingResponse;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest request, Long memberId) {
        // 유저 관련 정보 확인 및 해당 전화번호로 이미 예약된 사항이 있는 지 확인하는 로직
        // 예약된 사항이 있다면 예외 처리
        Waiting waiting = Waiting.builder()
                .member(memberId)
                .store(request.storeId())
                .type(request.waitingType())
                .mealType(request.mealType())
                .status(WaitingStatus.WAITING)
                .waitingNum()   // 대기 번호 부여 하는 법 고려
                .personnel(request.personnel())
                .build();

        waitingRepository.save(waiting);
        return WaitingResponse.from(waiting);
    }
}
