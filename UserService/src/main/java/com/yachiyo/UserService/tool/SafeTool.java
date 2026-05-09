package com.yachiyo.UserService.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SafeTool {

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public String generateCode(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        int randomNum = (int) (Math.random() * (max - min + 1)) + min;
        return String.valueOf(randomNum);
    }

    public String md5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public Mono<String> getUnique(Long userId) {
        // 1. 生成随机数
        int randomNum = new Random().nextInt(Integer.MAX_VALUE);
        String randomStr = String.valueOf(randomNum);
        String key = "user:" + userId;

        return reactiveRedisTemplate.opsForHash()
                .put(key, "unique", randomStr)
                .then(reactiveRedisTemplate.expire(key, Duration.ofHours(1)))
                .thenReturn(randomStr);
    }
}
