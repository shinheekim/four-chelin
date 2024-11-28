package com.example.fourchelin.domain.waiting.service;

import com.example.fourchelin.common.config.JPAConfiguration;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.entity.Waiting;
import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import com.example.fourchelin.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static com.example.fourchelin.domain.member.entity.Member.createMember;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(JPAConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConcurrencyIssueTest {

    @Autowired private WaitingRepository waitingRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    void testConcurrentWaitingNumber() {
        final WaitingService waitingService = new WaitingService(waitingRepository, storeRepository);
        Store store = storeRepository.saveAndFlush(new Store("name", "address"));

        int numberOfWaitings = 50;

        List<Member> members = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> createMember("0101234567" + i, "nickname", "password", MemberRole.USER))
                .toList();

        memberRepository.saveAllAndFlush(members);

        WaitingRequest request = new WaitingRequest(WaitingType.MOBILE, WaitingMealType.DINE_IN, 2, 1L);
        List<CompletableFuture<Void>> futures = members.stream()    // 병렬 처리(비동기 처리 공부하기)
                .map(member -> CompletableFuture.runAsync(() -> {
                    waitingService.create(request, member);
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<Waiting> waitings = waitingRepository.findAllByStoreIdAndStatus(1L, WaitingStatus.WAITING);
        assertThat(waitings).hasSize(numberOfWaitings);
        assertThat(waitings.stream()
                .map(Waiting::getWaitingNumber)
                .distinct())
                .hasSize(numberOfWaitings);
    }
}
