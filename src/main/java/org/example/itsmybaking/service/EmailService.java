package org.example.itsmybaking.service;

import org.example.itsmybaking.dto.EmailDetails;

public interface EmailService {//servic has a logic not needed an interface
    void sendEmailAlert(EmailDetails emailDetails);
}
