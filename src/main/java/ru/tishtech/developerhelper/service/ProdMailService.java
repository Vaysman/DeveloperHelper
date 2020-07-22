package ru.tishtech.developerhelper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ProdMailService implements MailService {

  @Autowired private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String username;

  @Override
  public void send(String toEmail, String subject, String text) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(username);
    simpleMailMessage.setTo(toEmail);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(text);
    javaMailSender.send(simpleMailMessage);
  }
}
