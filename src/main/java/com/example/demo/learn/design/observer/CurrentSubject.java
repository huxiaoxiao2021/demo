package com.example.demo.learn.design.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2024/2/27 4:40 PM
 */
public class CurrentSubject implements Subject {
    
    private static final List<Observer> list = new ArrayList<>();
    
    @Override
    public void registerObserver(Observer observer) {
        list.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        list.remove(observer);
    }

    @Override
    public void noticeAll() {
        for (Observer observer : list) {
            observer.update();
        }
    }
}
