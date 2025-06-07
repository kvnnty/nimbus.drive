package drive.nimbus.api.v1.services.mail.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import drive.nimbus.api.v1.services.mail.MailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
  private final JavaMailSender mailSender;
  // private final TemplateEngine templateEngine;

  @Async
  @Override
  public void sendMail(String to, String subject, String message) {

    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo(to);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);

    mailSender.send(mailMessage);
  }

}
