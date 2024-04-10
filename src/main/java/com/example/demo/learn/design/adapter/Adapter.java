package com.example.demo.learn.design.adapter;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2024/3/18 5:10 PM
 */
public class Adapter implements Interface110V{

    private Interface220V interface220V;

    public Adapter() {
    }

    public Adapter(Interface220V interface220V) {
        this.interface220V = interface220V;
    }

    @Override
    public void charge110V() {
        interface220V.charge220V(); // 当客户调用charge110V方法时委托给interface220V对象
    }

    public Interface220V getInterface220V() {
        return interface220V;
    }

    public void setInterface220V(Interface220V interface220V) {
        this.interface220V = interface220V;
    }
}
