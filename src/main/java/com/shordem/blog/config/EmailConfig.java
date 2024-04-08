package com.shordem.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import com.shordem.blog.service.EmailService;

@Configuration
public class EmailConfig {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Bean
    public EmailService emailService() {
        return new EmailService(javaMailSender, templateEngine);
    }
}
