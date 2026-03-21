package com.yachiyo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data @TableName("posting")
public class Posting {

    private Long id;

    private Long userId;

    private String title;

    private String content;

    private String type;
}
