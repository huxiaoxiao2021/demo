package com.example.demo.learn.design.adapter;

/**
 * 110V电脑
 *
 * @author hujiping
 * @date 2024/3/18 5:07 PM
 */
public class Computer110V {
    private Interface110V interface110V;

    public Computer110V() {
    }

    public Computer110V(Interface110V interface110V) {
        this.interface110V = interface110V;
    }

    public void charge() {
        interface110V.charge110V();
    }
    public void setInterface110V(Interface110V interface110V) {
        this.interface110V = interface110V;
    }
}
