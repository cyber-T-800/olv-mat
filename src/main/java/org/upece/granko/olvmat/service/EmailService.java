package org.upece.granko.olvmat.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.entity.enums.TypListkaEnum;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailTemplateService mailTemplateService;

    @Value("${mail.from}")
    private String from;
    @Value("${hostport}")
    private String hostport;


    public void odosliListok(TicketEntity entity) {

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("krstneMeno", entity.getMeno().split(" ")[0]);
        mailModel.put("ticketId", entity.getId());
        mailModel.put("securityKey", entity.getSecurityKey());
        mailModel.put("hostPort", hostport);
        if (entity.getTypListka() == TypListkaEnum.DOBROVOLNIK) {
            mailModel.put("dobrovolnik", true);
        }
        sendMail(entity.getEmail(), "Potvrdenie rezervácie listka", "potvrdenie-rezervacie", mailModel);

    }

    public void sendMail(String to, String subject, String template, Map<String, Object> model, byte[] qrImage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from, "Granko tím");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailTemplateService.generateHtml(template, model), true);
            if (qrImage != null) {
                helper.addInline("qrImage", new ByteArrayResource(qrImage), "image/png");
            }

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendMail(String to, String subject, String template, Map<String, Object> model) {
        sendMail(to, subject, template, model, null);
    }
}
