package com.example.fourchelin.domain.waiting.service;

import com.example.fourchelin.domain.member.entity.Member;
import com.example.fourchelin.domain.member.enums.MemberRole;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.store.enums.StoreCategory;
import com.example.fourchelin.domain.store.enums.StoreStatus;
import com.example.fourchelin.domain.store.repository.StoreRepository;
import com.example.fourchelin.domain.waiting.dto.request.WaitingRequest;
import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import com.example.fourchelin.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WaitingServiceConcurrentTest {
    @Autowired
    private WaitingService waitingService;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private RedissonClient redissonClient;

    private Long storeId;

    @BeforeEach
    void setup() {
        storeId = storeRepository.save(new Store(1L, "name", StoreStatus.OPEN, StoreCategory.분식, "address", 3)).getId();
    }

    @Test
    void testConcurrentWaitingCreation() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads);

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            long memberId = i + 1;
            members.add(new Member(memberId, "0101234567" + i, "nickname" + i, "password", MemberRole.USER));
        }

        for (Member member : members) {
            executorService.submit(() -> {
                try {
                    barrier.await();
                    waitingService.create(
                            new WaitingRequest(WaitingType.MOBILE, WaitingMealType.DINE_IN, 4, storeId),
                            member
                    );
                } catch (Exception e) {
                    System.err.println("Exception: " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(10, TimeUnit.SECONDS);

        // 검증
        assertThat(finished).isTrue(); // 모든 작업이 완료되었는지 확인
        assertThat(waitingRepository.countByStoreIdAndStatus(storeId, WaitingStatus.WAITING)).isEqualTo(100);

        waitingRepository.findAllByStoreIdAndStatus(storeId, WaitingStatus.WAITING)
                .forEach(waiting -> {
                    System.out.println("Waiting Number: " + waiting.getWaitingNumber());
                });
    }

}
