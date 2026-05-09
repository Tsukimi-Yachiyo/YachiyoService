package com.yachiyo.UserService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Date;

@Data @Table("users")
@AllArgsConstructor @NoArgsConstructor
public class User {
    
    /**
     * 用户ID
     */
    @Id
    @Column("user_id")
    Long id;
    
    /**
     * 用户名
     */
    String name;

    /**
     * 密码
     */
    String password;

    /**
     * 角色
     */
    String role;

    /**
     * 邮箱
     */
    String email;

    /**
     * 是否在线
     */
    Boolean isOnline;

    /**
     * 是否禁用
     */
    Boolean isLocked;
    
    /**
     * 创建时间
     */
    LocalDate  createTime;

    /**
     * 更新时间
     */
    LocalDate updateTime;
}
