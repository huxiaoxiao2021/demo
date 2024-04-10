package com.example.demo.learn.design.single;

/**
 * 设计模式-单例模式
 *  1、私有构造方法
 *  2、静态实例
 *
 * @author hujiping
 * @date 2023/7/4 11:28 AM
 */

/**
 * 饿汉式-线程安全
 */
public class SingleObject {

    //创建 SingleObject 的一个对象
    private static final SingleObject instance = new SingleObject();

    //让构造函数为 private，这样该类就不会被实例化
    private SingleObject() {
    }

    //获取唯一可用的对象
    public static SingleObject getInstance() {
        return instance;
    }
}

/**
 * 懒汉式-线程不安全
 */
class SingleObject1 {

    private static SingleObject1 instance;

    private SingleObject1() {
    }

    public static SingleObject1 getInstance() {
        if(instance == null){
            instance = new SingleObject1();
        }
        return instance;
    }
}

/**
 * 懒汉式-线程安全
 */
class SingleObject2 {

    private static SingleObject2 instance;

    private SingleObject2() {
    }

    public static synchronized SingleObject2 getInstance() {
        if(instance == null){
            instance = new SingleObject2();
        }
        return instance;
    }
}

/**
 * 双检锁/双重校验锁（DCL，即 double-checked locking）
 *  描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 */
class SingleObject3 {

    private static volatile SingleObject3 instance;

    private SingleObject3() {
    }

    public static SingleObject3 getInstance() {
        if(instance == null){
            synchronized (SingleObject3.class){
                if(instance == null){
                    instance = new SingleObject3();
                }
            }
        }
        return instance;
    }
}

/**
 * 登记式/静态内部类
 *  描述：这种方式能达到双检锁方式一样的功效，但实现更简单。对静态域使用延迟初始化，应使用这种方式而不是双检锁方式。这种方式只适用于静态域的情况，双检锁方式可在实例域需要延迟初始化时使用。
 */
class SingleObject4 {

    private static class SingletonHolder {
        private static final SingleObject4 INSTANCE = new SingleObject4();
    }

    private SingleObject4() {
    }

    public static SingleObject4 getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
