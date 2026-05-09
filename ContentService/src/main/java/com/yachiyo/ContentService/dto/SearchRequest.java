package com.yachiyo.ContentService.dto;

import lombok.Data;

@Data
public class SearchRequest {

    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
}
