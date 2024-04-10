package com.example.demo.learn.design.limit;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2024/3/22 5:35 PM
 */
public class TokenBucket {

    private long lastRefillTime = System.currentTimeMillis();
    private int capacity;
    private int tokens;
    private double refillRate; // tokens per millisecond

    public TokenBucket(int capacity, double refillRate) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRate = refillRate;
    }

    public synchronized boolean allowRequest(int tokensRequested) {
        refill();
        if (tokens >= tokensRequested) {
            tokens -= tokensRequested;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        if (now > lastRefillTime) {
            long timePassed = now - lastRefillTime;
            int tokensToRefill = (int)(timePassed * refillRate);
            tokens = Math.min(tokens + tokensToRefill, capacity);
            lastRefillTime = now;
        }
    }
    
}
