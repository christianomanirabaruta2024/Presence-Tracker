package com.upg.employee_management.service;

import com.upg.employee_management.aop.Trackable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Trackable
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAbsenceNotification(String to, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Absence Alert: 2 Consecutive Days");
        mailMessage.setText(message);
        mailMessage.setFrom("your-email@gmail.com");
        mailSender.send(mailMessage);
    }

    public void sendLeaveRequestNotification(String toEmail, String employeeName, String leaveType, String startDate, String endDate, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Leave Request Status Update");
        message.setText(String.format(
                "Dear %s,\n\nYour leave request (%s) from %s to %s has been %s.\n\nRegards,\nEmployee Management System",
                employeeName, leaveType, startDate, endDate, status));
        mailSender.send(message);
    }
}
