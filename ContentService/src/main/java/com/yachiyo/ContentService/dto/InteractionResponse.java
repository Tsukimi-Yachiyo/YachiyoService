package com.yachiyo.ContentService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class InteractionResponse {

    private Long coin;

    private Long like;
}
