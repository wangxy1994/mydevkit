package com.wangxy.exoskeleton;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wangxy.exoskeleton.mapper")
public class ExoskeletonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExoskeletonApplication.class, args);
    }

}
