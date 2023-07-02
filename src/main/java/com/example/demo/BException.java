package com.example.demo;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2023/4/4 2:37 PM
 */
public class BException extends RuntimeException{
    public BException(String message){
        super(message);
    }
    public Throwable fillInStackTrace() {
        return this;
    }
}
