package com.yachiyo.UserService.controller;

import com.yachiyo.UserService.dto.UserDetailDTO;
import com.yachiyo.UserService.dto.UserDetailType;
import com.yachiyo.UserService.result.Result;
import com.yachiyo.UserService.service.UserDetailGetService;
import com.yachiyo.UserService.service.UserInteractService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
@Validated
public class PublicUserDetailController {

    private final UserInteractService userInteractService;

    private final UserDetailGetService userDetailGetService;

    /**
     * 获取用户详情
     * @param userId 用户ID
     * @param userDetailType 用户详情类型 (不可为 SELF )
     * @return 用户详情
     */
    @GetMapping("/detail/{detail_type}")
    public Mono<Result<UserDetailDTO>> getUserDetail(@AuthenticationPrincipal String selfId,
                                                     @RequestParam Long userId,
                                                     @PathVariable("detail_type") UserDetailType userDetailType) {
        if (UserDetailType.SELF.equals(userDetailType) && !selfId.equals(userId.toString())) {
            return Mono.just(Result.error("400", "权限不足", "不可直接通过此接口获取完整私有详情"));
        }
        return userDetailGetService.getDetail(userId, Long.parseLong(selfId), userDetailType);
    }

    /**
     * 搜索用户
     * @param userName 用户名
     * @return 用户搜索结果
     */
    @PostMapping("/search")
    public Flux<Result<UserDetailDTO>> searchUser(@AuthenticationPrincipal String userId,
                                                  @RequestParam String userName,
                                                  @RequestParam int pageNum,
                                                  @RequestParam int pageSize) {
        return userDetailGetService.searchUser(Long.parseLong(userId), userName, pageNum, pageSize);
    }

    /**
     * 获取用户关注列表
     * @return 用户关注列表
     */
    @GetMapping("/followee")
    public Flux<Result<Long>> getFolloweeList(@AuthenticationPrincipal String userId) {
        return userInteractService.getFolloweeList(Long.parseLong(userId));
    }

    /**
     * 获取用户粉丝列表
     * @return 用户粉丝列表
     */
    @GetMapping("/follower")
    public Flux<Result<Long>> getFollowerList(@AuthenticationPrincipal String userId) {
        return userInteractService.getFollowerList(Long.parseLong(userId));
    }

    /**
     * 关注用户
     * @param userId 用户ID
     * @return 是否关注成功
     */
    @PostMapping("/follow")
    public Mono<Result<Boolean>> follow(@AuthenticationPrincipal @NonNull String userId,
                                        @RequestParam @NonNull Long followeeId) {
        return userInteractService.follow(Long.parseLong(userId), followeeId);
    }

}
