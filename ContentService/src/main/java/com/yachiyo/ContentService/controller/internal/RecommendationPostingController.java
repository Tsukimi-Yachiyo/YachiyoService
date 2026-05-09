package com.yachiyo.ContentService.controller.internal;

import com.yachiyo.ContentService.service.RecommendationPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/posting")
@RequiredArgsConstructor
@Validated
public class RecommendationPostingController {

    private final RecommendationPostingService recommendationPostingService;

    @PostMapping("/recommend")
    public void recommend() {
        recommendationPostingService.recommendPosting();
    }
}
