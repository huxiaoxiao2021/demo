package com.example.demo.learn.design.limit;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 类的描述
 *
 * @author hujiping
 */
public class TokenBucket1 {

    private int capacity; // 令牌桶容量
    private int tokens; // 当前令牌数量
    private long lastRefillTime; // 上次填充令牌的时间
    private int refillRate; // 每秒填充的令牌数量
    private Queue<Object> queue; // 令牌队列

    public TokenBucket1(int capacity, int refillRate) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
        this.queue = new LinkedList<>();
    }

    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastRefillTime;
        int tokensToAdd = (int) (elapsedTime / 1000 * refillRate);
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTime = now;
    }

    public static void main(String[] args) {
        TokenBucket1 tokenBucket = new TokenBucket1(10, 2); // 容量为10，每秒填充2个令牌
        for (int i = 0; i < 15; i++) {
            if (tokenBucket.allowRequest()) {
                System.out.println("Request " + (i + 1) + ": Allowed");
            } else {
                System.out.println("Request " + (i + 1) + ": Denied");
            }
        }
    }
    
}
