package com.vicentedev.api_re.service;

import com.vicentedev.api_re.entity.Role;

public interface EmailService {

    void send2FaCode(String toEmail, String userName, String code);

    void sendNewUserRegisteredAlert(String newUserName, String newUserEmail, Role role);
}
