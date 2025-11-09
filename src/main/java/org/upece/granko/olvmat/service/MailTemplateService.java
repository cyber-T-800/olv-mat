package org.upece.granko.olvmat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailTemplateService {
    private final TemplateEngine templateEngine;


    public String generateHtml(String templateName, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model);
        return templateEngine.process("mail/" + templateName, context);
    }
}