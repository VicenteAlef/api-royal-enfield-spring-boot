package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.admin.notification-email}")
    private String adminNotificationEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void send2FaCode(String toEmail, String userName, String code) {
        log.info("Preparing 2FA OTP code email for: {} (User: {})", toEmail, userName);
        if (mailSender == null) {
            log.warn("JavaMailSender is not configured. 2FA Code for {}: [{}]", toEmail, code);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Royal Enfield - Codigo de Autenticacao (2FA)");
            message.setText(String.format(
                    "Ola, %s!\n\nSeu codigo de autenticacao para acesso ao painel Royal Enfield e:\n\n%s\n\nEste codigo expira em 10 minutos.\nSe voce nao solicitou este acesso, desconsidere esta mensagem.",
                    userName, code
            ));

            mailSender.send(message);
            log.info("2FA OTP code successfully sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send 2FA email to {}: {}. 2FA Code was [{}]", toEmail, e.getMessage(), code);
        }
    }

    @Override
    @Async
    public void sendNewUserRegisteredAlert(String newUserName, String newUserEmail, Role role) {
        log.info("Preparing new user alert notification for admin: {}", adminNotificationEmail);
        if (mailSender == null) {
            log.warn("JavaMailSender is not configured. New user alert: {} ({}) with role [{}]", newUserName, newUserEmail, role);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminNotificationEmail);
            message.setSubject("Royal Enfield - Novo Usuario Registrado no Painel");
            message.setText(String.format(
                    "Prezado Administrador,\n\nUm novo usuario realizou cadastro no Painel de Gestao:\n\n- Nome: %s\n- E-mail: %s\n- Perfil Inicial: %s\n\nAcesse o painel para gerenciar as permissoes deste usuario.",
                    newUserName, newUserEmail, role.name()
            ));

            mailSender.send(message);
            log.info("New user registration alert successfully sent to admin: {}", adminNotificationEmail);
        } catch (Exception e) {
            log.error("Failed to send admin notification email: {}", e.getMessage());
        }
    }
}
