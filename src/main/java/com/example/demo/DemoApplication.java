package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;


@Slf4j
@Configuration
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo"})
@PropertySource({"classpath:application.properties"})
@ImportResource({"classpath:spring/spring-util.xml"})
public class DemoApplication {

    public static void main(String[] args) {
        try {

            SpringApplication.run(DemoApplication.class, args);
        }catch (Exception e){
            log.error("WorkbenchControllerApplication start error",e);
        }
    }

}
