package com.example.DoAnJ2EE.service;

public interface MailService {
    void sendMail(String to, String subject, String html);
}