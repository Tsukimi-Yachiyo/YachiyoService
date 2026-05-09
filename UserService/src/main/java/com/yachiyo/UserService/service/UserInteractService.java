package com.yachiyo.UserService.service;

import com.yachiyo.UserService.dto.UserDetailDTO;
import com.yachiyo.UserService.result.Result;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInteractService {




    /**
     * 关注用户
     * @param userId 用户ID
     * @return 是否关注成功
     */
    Mono<Result<Boolean>> follow(Long userId, Long followeeId);

    /**
     * 获取用户关注列表
     * @param userId 用户ID
     * @return 用户关注列表
     */
    Flux<Result<Long>> getFolloweeList(Long userId);

    /**
     * 获取用户粉丝列表
     * @param userId 用户ID
     * @return 用户粉丝列表
     */
    Flux<Result<Long>> getFollowerList(Long userId);

    /**
     * 判断用户是否是好友
     * @param currentUserId 当前用户ID
     * @param followeeId 被关注用户ID
     * @return 是否是好友
     */
    Mono<Result<Boolean>> isFriend(Long currentUserId, Long followeeId);

    /**
     * 获取用户好友列表
     * @param currentUserId 当前用户ID
     * @return 用户好友列表
     */
    Flux<Result<Long>> friends(Long currentUserId);
}
