package com.shordem.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMail(String sender, String to, String subject, String templateName, Context context)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        String from = "<" + sender + "> Shordem Blog";

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(from);

        String html = templateEngine.process(templateName, context);
        helper.setText(html, true);

        javaMailSender.send(mimeMessage);
    }
}
