package com.yachiyo.UserService.init;

import com.yachiyo.UserService.task.DateScheduledConfigTask;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Init {

    @Autowired
    DateScheduledConfigTask dateScheduledConfigTask;

    @PostConstruct
    public void init() {
        dateScheduledConfigTask.dateScheduledConfigTask();
    }
}
