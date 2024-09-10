package org.example.itsmybaking.service;

import ch.qos.logback.core.CoreConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.itsmybaking.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {
//    @Autowired
    private JavaMailSender JavamailSender;

    @Value("${spring.mail.username}")
    private String SenderMail;

    @Autowired
    public EmailServiceImplementation(JavaMailSender javaMailSender) {
        this.JavamailSender = javaMailSender;
    }

    public void sendEmailAlert(SimpleMailMessage message) {
        JavamailSender.send(message);
    }

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(SenderMail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());
            JavamailSender.send(mailMessage);
            System.out.println("Email sent successfully");
       }
        catch(MailException e){
            throw new RuntimeException(e);
        }}

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = JavamailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        JavamailSender.send(mimeMessage);
    }
    }

