package com.zl.yxt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(value = "com.zl.yxt.mapper")
@EnableTransactionManagement
public class YxtApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxtApplication.class, args);
    }
}
