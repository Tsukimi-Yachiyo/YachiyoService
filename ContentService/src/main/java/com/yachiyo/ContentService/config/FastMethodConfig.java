package com.yachiyo.ContentService.config;

import com.github.kwfilter.util.KeyWordFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastMethodConfig {

    @Bean
    public KeyWordFilter keyWordFilter() {
        return KeyWordFilter.getInstance();
    }

}
