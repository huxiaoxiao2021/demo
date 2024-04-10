package com.example.demo.learn.design.adapter;

/**
 * 220V充电器
 *
 * @author hujiping
 * @date 2024/3/18 5:10 PM
 */
public class Charger220V implements Interface220V {
    @Override
    public void charge220V() {
        System.out.println("使用220V充电");
    }
}
