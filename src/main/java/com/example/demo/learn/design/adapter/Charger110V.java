package com.example.demo.learn.design.adapter;

/**
 * 110V充电器
 *
 * @author hujiping
 * @date 2024/3/18 5:06 PM
 */
public class Charger110V implements Interface110V {
    @Override
    public void charge110V() {
        System.out.println("使用110V充电");
    }
}
