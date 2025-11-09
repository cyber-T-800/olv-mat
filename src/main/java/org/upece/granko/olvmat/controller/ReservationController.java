package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.model.ReservationForm;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.service.EmailService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @GetMapping("")
    public String rezervacia() {
        return "rezervacia";
    }

    @PostMapping("")
    public String odosliRezervaciu(ReservationForm form) {

        ticketRepository.save(new TicketEntity(form.getName(), form.getEmail()));
        Map<String, Object> model = new HashMap<>();
        emailService.sendMail(form.getEmail(), "Potvrdenie rezervácie listka", "mail-potvrdenie", model);

        return "rezervacia";
    }
}
