package com.example.demo.jsf;

import com.jd.jsf.gd.msg.ResponseListener;
public interface GenericService {
    /**
     * 泛化调用
     *
     * @param method
     *         方法名
     * @param parameterTypes
     *         参数类型
     * @param args
     *         参数列表
     * @return 返回值
     */
    public Object $invoke(String method, String[] parameterTypes, Object[] args);
    /**
     * 异步回调的泛化调用
     *
     * @param method
     *         方法名
     * @param parameterTypes
     *         参数类型
     * @param args
     *         参数列表
     * @param listener
     *         结果listener
     */
    public void $asyncInvoke(String method, String[] parameterTypes, Object[] args, ResponseListener listener);
}