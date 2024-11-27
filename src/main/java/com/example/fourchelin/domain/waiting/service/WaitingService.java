package com.example.fourchelin.domain.waiting.service;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.exception.StoreException;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.dto.response.WaitingInfoResponse;
import com.example.fourchelin.domain.waiting.dto.response.WaitingResponse;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.exception.WaitingAlreadyExistException;
import com.example.fourchelin.domain.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.fourchelin.domain.waiting.enums.WaitingStatus.fromStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest request, Member member) {
        checkWaiting(member.getId(), request.storeId());

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new StoreException("해당 가게가 존재하지 않습니다."));

        Long waitingCount = waitingRepository.countByStoreIdAndStatus(store.getId(), WaitingStatus.WAITING);

        Waiting waiting = Waiting.builder()
                .member(member)
                .store(store)
                .type(request.waitingType())
                .mealType(request.mealType())
                .status(WaitingStatus.WAITING)
                .waitingNumber(waitingCount + 1)
                .personnel(request.personnel())
                .build();

        waitingRepository.save(waiting);
        return WaitingResponse.from(waiting);
    }

    public List<WaitingInfoResponse> retrieveInfoListByStatus(Member member, String visit) {
        // TODO: 방문 카테고리(방문완료, 방문예정, 취소)에 따른 웨이팅 정보 조회
        List<Waiting> waitings = waitingRepository.findAllByMemberIdAndStatus(member.getId(), fromStatus(visit));

        return waitings.stream()
                .map(WaitingInfoResponse::from)
                .toList();
    }

    private void checkWaiting(Long memberId, Long storeId) {
        if (waitingRepository.existsByMemberIdAndStoreIdAndStatus(memberId, storeId, WaitingStatus.WAITING)) {
            throw new WaitingAlreadyExistException("이미 예약된 사항이 존재합니다.");
        }
    }
}
