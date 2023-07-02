package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2022/9/30 2:46 PM
 */
@RestController
public class TestController {

    @RequestMapping("/hello")
    public String get(){
        return "hello Spring Boot!";
    }
}
