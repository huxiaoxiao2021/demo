package com.example.demo.learn.design.observer;

import org.springframework.stereotype.Service;

/**
 * 观察者B
 *
 * @author hujiping
 * @date 2024/2/27 4:35 PM
 */
@Service
public class ObserverB implements Observer {

    @Override
    public void update() {
        System.out.println("观察者B已接收到。。。");
    }
}
