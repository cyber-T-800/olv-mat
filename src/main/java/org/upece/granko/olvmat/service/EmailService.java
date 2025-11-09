package org.upece.granko.olvmat.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailTemplateService mailTemplateService;

    @Value("${mail.from}")
    private String from;

    public void sendMail(String to, String subject, String template, Map<String, Object> model) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(from, "Granko tím");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailTemplateService.generateHtml(template, model), true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
