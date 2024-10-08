package com.conseller.conseller.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.servlet.mail.host}")
    private String host;

    @Value("${spring.servlet.mail.port}")
    private int port;

    @Value("${spring.servlet.mail.username}")
    private String username;

    @Value("${spring.servlet.mail.password}")
    private String password;

    @Value("${spring.servlet.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.servlet.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${spring.servlet.mail.properties.mail.smtp.starttls.required}")
    private String starttlsRequired;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", String.valueOf(auth));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttlsEnable));
        props.put("mail.smtp.starttls.required", String.valueOf(starttlsRequired));

        return mailSender;
    }
}
