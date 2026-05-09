package com.yachiyo.UserService.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component @Slf4j
public class MailUtils {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public MailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Mono<Void> sendMail(String to, String subject, String content) {
        return Mono.fromRunnable(() -> {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);
                mailSender.send(mimeMessage);
                log.info("邮件发送成功");
            } catch (MessagingException e) {
                log.error("邮件发送失败", e);
                throw new RuntimeException("邮件发送失败", e);
            }
        });
    }
}
