package com.yachiyo.UserService.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yachiyo.UserService.tool.SensitiveWordFilter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data @Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailDTO {

    /**
     * 用户昵称
     */
    @SensitiveWordFilter(message = "用户名包含敏感词")
    String userName;

    /**
     * 用户介绍
     */
    @SensitiveWordFilter(message = "用户介绍包含敏感词")
    String userIntroduction;
    
    /**
     * 用户城市
     */
    @SensitiveWordFilter(message = "城市包含敏感词")
    String userCity;

    /**
     * 用户头像
     */
    String userAvatar;

    /**
     * 用户性别
     */
    String userGender;

    /**
     * 用户手机号
     */
    String userPhone;

    /**
     * 用户QQ
     */
    String userQQ;

    /**
     * 用户邮箱
     */
    String userMail;

    /**
     * 用户生日
     */
    LocalDate userBirthday;

    /**
     * 关注者数
     */
    private Long followerCount;

    /**
     * 被关注者数
     */
    private Long followeeCount;

    /**
     * 是否关注
     */
    private Boolean isFollowing;

    /**
     * 是否被关注
     */
    private Boolean isFollowed;
}
