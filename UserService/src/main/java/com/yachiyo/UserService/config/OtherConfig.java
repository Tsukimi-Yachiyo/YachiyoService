package com.yachiyo.UserService.config;

import com.github.kwfilter.util.KeyWordFilter;
import com.yachiyo.UserService.result.Result;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Supplier;

@Configuration @Slf4j
public class OtherConfig {

    @Bean
    public KeyWordFilter keyWordFilter() {
        return KeyWordFilter.getInstance();
    }

    @Bean
    public SpringFormEncoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
}
