package com.example.demo.demo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2024/4/9 6:06 PM
 */
public class Demo {
    
    public static void main(String[] args) {
        int [] array = {1,2,3,4,5};
        queryResult(array);
        
        // 1：1
        // 2：1,2,12
        // 3：1,2,3,12,13,23,123
        // 1,2,3,4
        // 12,13,14,23,24
        // 123,124,234
        // 1234
        
        // 1个数+2个数+3个数+4个数+5个数
    }
    
    public static List<String> queryResult(int [] array){
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < array.length; i ++) {
            int count = array.length - i-1;
            for (int loop = 1; loop < count; loop ++) {

            }
            
        }
        
        
        return null;
    } 
}
