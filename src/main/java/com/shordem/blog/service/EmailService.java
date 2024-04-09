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
    private String defaultSender;

    @Autowired
    public EmailService(String defaultSender, JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.defaultSender = defaultSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMail(String to, String subject, String templateName, Context context)
            throws MessagingException {
        sendMail(defaultSender, to, subject, templateName, context);
    }

    public void sendMail(String sender, String to, String subject, String templateName, Context context)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(sender);

        String html = templateEngine.process(templateName, context);
        helper.setText(html, true);

        javaMailSender.send(mimeMessage);
    }
}
