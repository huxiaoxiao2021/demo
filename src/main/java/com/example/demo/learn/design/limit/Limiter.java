package com.example.demo.learn.design.limit;

/**
 * 令牌桶限流
 *
 * @author hujiping
 * @date 2024/3/22 5:35 PM
 */
public class Limiter {

    public static void main(String[] args) {
        TokenBucket tokenBucket = new TokenBucket(100, 0.5); // 桶容量为100，每毫秒填充0.5个令牌
        for (int i = 0; i < 10; i++) {
            if (tokenBucket.allowRequest(10)) {
                System.out.println("Allow request " + i);
            } else {
                System.out.println("Reject request " + i);
            }
            try {
                Thread.sleep(100); // 模拟请求间隔
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
