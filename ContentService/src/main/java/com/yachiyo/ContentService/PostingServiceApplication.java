package com.yachiyo.ContentService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@MapperScan("com.yachiyo.ContentService.mapper")
@EnableFeignClients(basePackages = "com.yachiyo.ContentService.client")
public class PostingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostingServiceApplication.class, args);
    }
}
