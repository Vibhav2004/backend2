package com.swipenow.swipenow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("t20798028@gmail.com"); // your SMTP login
            message.setTo(toEmail);
            message.setSubject("Your OTP Verification Code");
            message.setText("Your OTP is: " + otp + "\nThis OTP is valid for 5 minutes.\nPlease do NOT share this OTP with anyone <3");

            mailSender.send(message);
            System.out.println("OTP email sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email to " + toEmail);

        }
    }
}
