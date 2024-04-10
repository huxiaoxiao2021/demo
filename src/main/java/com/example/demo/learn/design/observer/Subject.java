package com.example.demo.learn.design.observer;

/**
 * 主题
 *
 * @author hujiping
 * @date 2024/2/27 4:33 PM
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void noticeAll();
}
