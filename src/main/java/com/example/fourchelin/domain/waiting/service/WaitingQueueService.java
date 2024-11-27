package com.example.fourchelin.domain.waiting.service;

import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class WaitingQueueService {
    private final Queue<Long> waitingQueue = new ConcurrentLinkedQueue<>();

    public void addToQueue(Long memberId) {
        waitingQueue.offer(memberId);
    }

    public boolean removeFromQueue(Long memberId) {
        return waitingQueue.remove(memberId);
    }

    public Long pollFromQueue() {
        return waitingQueue.poll();
    }

    public int sizeOfQueue() {
        return waitingQueue.size();
    }

    public void clearQueue() {
        waitingQueue.clear();
    }
}
