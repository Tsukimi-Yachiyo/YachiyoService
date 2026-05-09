package com.yachiyo.ContentService.dto;

import com.yachiyo.ContentService.tool.SensitiveWordFilter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {

    private Long postingId;

    @NotBlank(message = "评论内容不能为空")
    @SensitiveWordFilter(message = "评论内容包含敏感词")
    private String content;
}
