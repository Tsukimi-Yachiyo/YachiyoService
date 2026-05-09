package com.yachiyo.UserService.service.Impl;

import com.yachiyo.UserService.entity.FollowLink;
import com.yachiyo.UserService.result.Result;
import com.yachiyo.UserService.service.UserInteractService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor @Slf4j
public class UserInteractServiceImpl implements UserInteractService {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<Result<Boolean>> follow(Long userId, Long followeeId) {

        if (followeeId.equals(userId)) {
            return Mono.just(Result.error("400", "不能关注自己", "不能关注自己"));
        }

        FollowLink followLink = new FollowLink();
        followLink.setFollowerId(userId);
        followLink.setFolloweeId(followeeId);

        return template.select(FollowLink.class).matching(Query.query(Criteria.where("follower").is(userId).and("followee_id").is(followeeId)))
                .exists()
                .flatMap(exists -> exists ? template.delete(FollowLink.class).all() : template.insert(followLink))
                .map(_ -> Result.success(true))
                .onErrorResume(error -> Mono.just(Result.error("500", error.getMessage(), "关注失败")));
    }

    @Override
    public Flux<Result<Long>> getFolloweeList(Long userId) {
        return template.select(FollowLink.class)
                .matching(Query.query(Criteria.where("follower").is(userId)).columns("followee"))
                .all().mapNotNull(FollowLink::getFolloweeId)
                .map(Result::success)
                .onErrorResume(error -> Flux.just(Result.error("500", error.getMessage(), "获取关注列表失败")));
    }

    @Override
    public Flux<Result<Long>> getFollowerList(Long userId) {
        return template.select(FollowLink.class)
                .matching(Query.query(Criteria.where("followee").is(userId)).columns("follower"))
                .all().mapNotNull(FollowLink::getFollowerId)
                .map(Result::success)
                .onErrorResume(error -> Flux.just(Result.error("500", error.getMessage(), "获取粉丝列表失败")));
    }

    @Override
    public Mono<Result<Boolean>> isFriend(Long currentUserId, Long followeeId) {
        Mono<Boolean> isFollowingMono = template.select(FollowLink.class)
                .matching(Query.query(Criteria.where("follower").is(currentUserId).and("followee").is(followeeId)))
                .exists()
                .defaultIfEmpty(false);

        Mono<Boolean> isFollowedMono = template.select(FollowLink.class)
                .matching(Query.query(Criteria.where("followee").is(currentUserId).and("follower").is(followeeId)))
                .exists()
                .defaultIfEmpty(false);

        return Mono.zip(isFollowingMono, isFollowedMono)
                .map(tuple -> {
                    boolean following = tuple.getT1();
                    boolean followed = tuple.getT2();
                    return Result.success(following && followed);
                });
    }

    @Override
    public Flux<Result<Long>> friends(Long currentUserId) {

        return getFolloweeList(currentUserId)
                .map(Result::getData)
                .flatMap(
                followeeId -> template.select(FollowLink.class)
                        .matching(Query.query(Criteria.where("followee").is(currentUserId).and("follower").is(followeeId)))
                        .exists()
                        .defaultIfEmpty(false)
                        .filter(Boolean::booleanValue)
                        .thenReturn(Result.success(followeeId))
                        .onErrorResume(error -> Mono.just(Result.error("500", error.getMessage(), "获取关注状态失败")))
                );

    }
}
