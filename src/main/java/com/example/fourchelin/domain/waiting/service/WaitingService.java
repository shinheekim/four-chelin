package com.example.fourchelin.domain.waiting.service;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
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
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest request, Member member) {
        checkWaiting(member);

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new StoreException("해당 가게가 존재하지 않습니다."));
        Waiting waiting = Waiting.builder()
                .member(member)
                .store(store)
                .type(request.waitingType())
                .mealType(request.mealType())
                .status(WaitingStatus.WAITING)
                .waitingNumber(1)   // 대기 번호 부여 하는 법 고려
                .personnel(request.personnel())
                .build();

        waitingRepository.save(waiting);
        return WaitingResponse.from(waiting);
    }

    private void checkWaiting(Member member) {
        if (waitingRepository.existsByMemberAndStatus(member, WaitingStatus.WAITING)) {
            throw new IllegalArgumentException("이미 예약된 사항이 존재합니다.");
        }
    }
}
