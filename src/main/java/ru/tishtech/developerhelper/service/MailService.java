package ru.tishtech.developerhelper.service;

public interface MailService {

  void send(String toEmail, String subject, String text);
}
