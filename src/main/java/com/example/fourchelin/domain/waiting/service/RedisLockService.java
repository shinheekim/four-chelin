/*
package com.example.fourchelin.domain.waiting.service;

import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {
    private final RedissonClient redissonClient;

    public RedisLockService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void executeWithLock(String lockKey, Runnable task) {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 시도 (10초 동안 기다리고 5초 동안 유지)
            if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                task.run();
            } else {
                throw new IllegalStateException("Unable to acquire lock");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread interrupted while acquiring lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

*/
