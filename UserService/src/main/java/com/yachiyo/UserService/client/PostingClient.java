package com.yachiyo.UserService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "posting-service")
public interface PostingClient {

    /**
     * 推荐发布
     */
    @RequestMapping("/internal/posting/recommend")
    void recommendPosting();
}
