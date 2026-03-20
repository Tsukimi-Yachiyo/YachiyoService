package com.yachiyo.dto;

import lombok.Data;

@Data
public class PostEncapsulateResponse {

    private String title;

    private int posterId;

    private byte[] coverImage;
}
