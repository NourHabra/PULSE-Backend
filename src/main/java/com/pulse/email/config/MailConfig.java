package com.pulse.email.config;


import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


import org.springframework.boot.context.properties.EnableConfigurationProperties;


@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {

    @Autowired
    private MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());


        Properties props = mailSender.getJavaMailProperties();
        props.putAll(mailProperties.getProperties());

        props.put("mail.transport.protocol", "smtp");

        return mailSender;
    }
}
