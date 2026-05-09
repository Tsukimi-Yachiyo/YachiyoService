package com.yachiyo.UserService.controller.internal;

import com.yachiyo.UserService.dto.MailRequest;
import com.yachiyo.UserService.service.ManageUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/internal/manage/user")
@RequiredArgsConstructor
public class ManageUserController {

    private final ManageUserService manageUserService;

    @PostMapping("send_mail")
    public Mono<Boolean> sendMail(@RequestBody MailRequest mailRequest){
        return manageUserService.SendEmail(mailRequest.getTitle(), mailRequest.getContent());
    }
}
