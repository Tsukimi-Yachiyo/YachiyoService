package com.yachiyo.entity;

import lombok.Data;

@Data
public class Comment {

    private Long id;
    private Long userId;
    private Long postingId;
    private String content;
}
