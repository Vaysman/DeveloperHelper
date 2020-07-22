package ru.tishtech.developerhelper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("dev")
@Service
@Primary
public class PrintToLogMailService implements MailService {

  @Value("${spring.mail.username}")
  private String username;

  public void send(String toEmail, String subject, String text) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(username);
    simpleMailMessage.setTo(toEmail);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(text);
    log.debug("Sending mail {}", simpleMailMessage);
  }
}
