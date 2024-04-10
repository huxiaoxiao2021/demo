package com.example.demo.learn.design.adapter;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2024/3/18 5:08 PM
 */
public class AdapterDemo {
    public static void main(String[] args) {
//        Computer110V computer110V = new Computer110V(); // 购买一台电脑
//        Charger110V charger110V = new Charger110V(); // 购买一个110V的充电器
//        computer110V.setInterface110V(charger110V); // 电脑插上110V的充电器
//        computer110V.charge(); // 电脑开始充电
        
        // ---------

        Computer110V computer110V = new Computer110V(); // 购买一台电脑
        Charger220V charger220V = new Charger220V(); // 购买一个220V的充电器
        Adapter adapter = new Adapter(charger220V); // 通过220V充电器产生一个适配器
        computer110V.setInterface110V(adapter); // 电脑插上适配器
        computer110V.charge(); // 电脑开始充电
    }
}
