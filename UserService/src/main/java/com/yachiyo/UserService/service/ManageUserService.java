package com.yachiyo.UserService.service;

import reactor.core.publisher.Mono;

public interface ManageUserService {

    /**
     * 发送邮件
     * @param email 邮箱
     * @return 发送结果
     */
    Mono<Boolean> SendEmail(String title, String email);
}
