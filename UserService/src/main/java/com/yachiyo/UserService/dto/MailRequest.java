package com.yachiyo.UserService.dto;

import lombok.Data;

@Data
public class MailRequest {

    // 邮件标题
    private String title;
    // 邮件内容
    private String content;
}
