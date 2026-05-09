package com.yachiyo.UserService.service;

import com.yachiyo.UserService.dto.UserDetailDTO;
import com.yachiyo.UserService.dto.UserDetailType;
import com.yachiyo.UserService.result.Result;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserDetailGetService {

    /**
     * 获取用户详情
     * @param userId 用户ID
     * @param userDetailType 用户详情类型
     * @return 用户详情
     */
    Mono<Result<UserDetailDTO>> getDetail(Long userId,Long selfID, UserDetailType userDetailType);

    /**
     * 搜索用户
     *
     * @param currentUserId 当前用户ID
     * @param userName 用户名
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 用户搜索结果
     */
    Flux<Result<UserDetailDTO>> searchUser(Long currentUserId, String userName, int pageNum, int pageSize);

}
