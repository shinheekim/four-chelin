package com.example.fourchelin.domain.waiting.service;

import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class WaitingQueueService {
    private final Queue<Long> waitingQueue = new ConcurrentLinkedQueue<>();  // 스레드 안정성

    // 대기열에 추가
    public void addToQueue(Long memberId) {
        waitingQueue.offer(memberId);
    }

    // 대기열에서 요소 제거
    public boolean removeFromQueue(Long memberId) {
        return waitingQueue.remove(memberId);
    }

    // 대기열 앞쪽 통과
    public Long pollFromQueue() {
        return waitingQueue.poll();
    }

    // 대기열 전체 크기 조회
    public int sizeOfQueue() {
        return waitingQueue.size();
    }

    // 대기열 초기화
    public void clearQueue() {
        waitingQueue.clear();
    }
}
