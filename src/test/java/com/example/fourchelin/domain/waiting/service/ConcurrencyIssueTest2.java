package com.example.fourchelin.domain.waiting.service;

import com.example.fourchelin.common.config.JPAConfiguration;
import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;
import com.example.fourchelin.domain.member.repository.MemberRepository;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import com.example.fourchelin.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JPAConfiguration.class, WaitingService.class})
public class ConcurrencyIssueTest2 {
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WaitingService waitingService;

    private Store store;
    private List<Member> members;

    @BeforeEach
    void setUp() {
        store = storeRepository.save(new Store("name", "address"));
        members = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Member member = Member.createMember("0101234567" + i, "nickname", "password", MemberRole.USER);
            members.add(memberRepository.save(member));
        }
    }

    @Test
    void testConcurrentWaitingNumberGeneration() throws InterruptedException{
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);

        List<Future<Long>> results = new ArrayList<>();
        WaitingRequest request = new WaitingRequest(WaitingType.MOBILE, WaitingMealType.DINE_IN, 2, store.getId());

        for (int i = 0; i < threadCount; i++) {
            Member member = members.get(i);
            results.add(executorService.submit(() -> {
                barrier.await();
                waitingService.create(request, member);
                return waitingRepository.countByStoreIdAndStatus(store.getId(), WaitingStatus.WAITING);
            }));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        List<Long> waitingNumbers = results.stream()
                .map(result -> {
                    try {
                        return result.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        System.out.println("waiting numbers: " + waitingNumbers);

        // 대기열에 정확히 100개의 예약이 생성되었는지 확인
        assertThat(waitingRepository.countByStoreIdAndStatus(store.getId(), WaitingStatus.WAITING))
                .isEqualTo(threadCount);
        assertThat(waitingNumbers).hasSize(threadCount);

        // 모든 쓰레드가 대기열 번호를 고유하게 받았는지 확인
        assertThat(waitingNumbers).doesNotHaveDuplicates();
    }
}
