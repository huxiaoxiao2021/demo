package com.example.demo.learn.design.observer;

/**
 * 观察者模式案例
 *
 * @author hujiping
 * @date 2024/2/27 4:42 PM
 */
public class ObserverDemo {
    
    public static void main(String[] args) {
        
        Observer observerA = new ObserverA();
        Observer observerB = new ObserverB();
        
        Subject currentSubject = new CurrentSubject();
        currentSubject.registerObserver(observerA);
        currentSubject.registerObserver(observerB);

        currentSubject.noticeAll();
    }
}
