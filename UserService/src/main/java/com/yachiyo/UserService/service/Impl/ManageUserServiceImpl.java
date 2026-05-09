package com.yachiyo.UserService.service.Impl;

import com.yachiyo.UserService.entity.User;
import com.yachiyo.UserService.service.ManageUserService;
import com.yachiyo.UserService.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ManageUserServiceImpl implements ManageUserService {

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private R2dbcEntityTemplate template;

    @Override
    public Mono<Boolean> SendEmail(String title, String email) {
        return template.select(User.class).all()
                .filter(user -> user.getEmail() != null && !user.getEmail().isEmpty())
                .flatMap(user -> mailUtils.sendMail(user.getEmail(), title, email)
                        .onErrorResume(e -> {
                            log.error("Failed to send email to {}", user.getEmail(), e);
                            return Mono.empty();
                        })
                )
                .then(Mono.just(true));
    }
}
