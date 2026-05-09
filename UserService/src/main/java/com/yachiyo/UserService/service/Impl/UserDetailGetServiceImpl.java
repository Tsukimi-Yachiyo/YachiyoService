package com.yachiyo.UserService.service.Impl;

import com.yachiyo.UserService.client.FileClient;
import com.yachiyo.UserService.dto.UserDetailDTO;
import com.yachiyo.UserService.dto.UserDetailType;
import com.yachiyo.UserService.entity.FollowLink;
import com.yachiyo.UserService.entity.UserDetail;
import com.yachiyo.UserService.result.Result;
import com.yachiyo.UserService.service.UserDetailGetService;
import com.yachiyo.UserService.tool.FileClientTool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailGetServiceImpl implements UserDetailGetService {

    private final R2dbcEntityTemplate template;

    private final FileClient fileClient;

    private final FileClientTool fileClientTool;

    private static final String AVATAR_PATH_FORMAT = "%d/avatar.jpg";

    @Override
    public Mono<Result<UserDetailDTO>> getDetail(Long userId,Long selfID, UserDetailType userDetailType) {
        List<UserDetailType> components = userDetailType.getBasicFields();
        UserDetailDTO userDetailDTO = new UserDetailDTO();

        Mono<UserDetail> userDetailCache = template.select(UserDetail.class)
                .matching(Query.query(Criteria.where("id").is(userId)))
                .one()
                .cache();

        return Flux.fromIterable(components)
                .flatMap(type -> fillData(userDetailDTO, type, userId,selfID, userDetailCache)) // 并行执行获取逻辑
                .then(Mono.just(Result.success(userDetailDTO)));
    }

    @Override
    public Flux<Result<UserDetailDTO>> searchUser(Long currentUserId, String userName, int pageNum, int pageSize) {
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1 || pageSize > 100) pageSize = 20;
        int offset = (pageNum - 1) * pageSize;

        return template.select(UserDetail.class)
                .matching(Query.query(Criteria.where("userName").like("%" + userName + "%"))
                     .limit(pageSize).offset(offset))
                .all()
                .flatMap(userDetail -> getDetail(userDetail.getUserId(), currentUserId, UserDetailType.SEARCH));
    }

    private Mono<UserDetailDTO> fillData(UserDetailDTO response, UserDetailType type, Long userId,Long selfID, Mono<UserDetail> userDetailCache) {

        return switch (type) {

            // 头像
            case AVATAR -> fileClientTool.callFileClient(
                    () -> Result.success(fileClient.getUrl(String.format(AVATAR_PATH_FORMAT, userId), System.currentTimeMillis())),
                    "获取头像URL失败").doOnNext(response::setUserAvatar).thenReturn(response);

            // 昵称
            case NAME -> extractAndSet(userDetailCache, response, UserDetail::getUserName, response::setUserName);

            // 个人介绍
            case INTRODUCTION -> extractAndSet(userDetailCache, response, UserDetail::getUserIntroduction, response::setUserIntroduction);

            // 城市
            case CITY -> extractAndSet(userDetailCache, response, UserDetail::getUserCity, response::setUserCity);

            // 性别
            case GENDER -> extractAndSet(userDetailCache, response, UserDetail::getUserGender, response::setUserGender);

            // 出生日期
            case BIRTHDAY -> extractAndSet(userDetailCache, response, UserDetail::getUserBirthday, response::setUserBirthday);

            // QQ号
            case QQ -> extractAndSet(userDetailCache, response, UserDetail::getUserQQ, response::setUserQQ);

            // 手机号
            case PHONE -> extractAndSet(userDetailCache, response, UserDetail::getUserPhone, response::setUserPhone);

            // 粉丝数量
            case FOLLOWER_COUNT -> template.select(FollowLink.class)
                    .matching(Query.query(Criteria.where("followee").is(userId)))
                    .count()
                    .doOnNext(response::setFollowerCount)
                    .thenReturn(response)
                    .defaultIfEmpty(response);

            // 关注数量
            case FOLLOWEE_COUNT -> template.select(FollowLink.class)
                    .matching(Query.query(Criteria.where("followee").is(userId)))
                    .count()
                    .doOnNext(response::setFolloweeCount)
                    .thenReturn(response)
                    .defaultIfEmpty(response);

            // 是否关注
            case IS_FOLLOWED -> template.select(FollowLink.class)
                    .matching(Query.query(Criteria.where("followee").is(selfID).and(Criteria.where("follower").is(userId))))
                    .count()
                    .map(count -> count > 0)
                    .doOnNext(response::setIsFollowed)
                    .thenReturn(response)
                    .defaultIfEmpty(response);

            // 是否关注
            case IS_FOLLOWING -> template.select(FollowLink.class)
                    .matching(Query.query(Criteria.where("followee").is(userId).and(Criteria.where("follower").is(selfID))))
                    .count()
                    .map(count -> count > 0)
                    .doOnNext(response::setIsFollowing)
                    .thenReturn(response)
                    .defaultIfEmpty(response);

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    /**
     * 从缓存中提取指定字段，并赋值给 DTO
     *
     * @param cache    缓存的 Mono<UserDetail>
     * @param response 需要返回和修改的 DTO
     * @param getter   如何从 UserDetail 获取字段的方法引用
     * @param setter   如何将字段设置进 DTO 的方法引用
     */
    private <T> Mono<UserDetailDTO> extractAndSet(
            Mono<UserDetail> cache,
            UserDetailDTO response,
            Function<UserDetail, T> getter,
            Consumer<T> setter) {

        return cache
                .mapNotNull(getter)
                .doOnNext(setter)
                .thenReturn(response)
                .defaultIfEmpty(response);
    }
}
