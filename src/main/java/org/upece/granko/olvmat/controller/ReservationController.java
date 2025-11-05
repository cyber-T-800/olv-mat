package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.model.ReservationForm;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.service.EmailService;

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
        emailService.sendMail(form.getEmail(), "Predaj lístka", "Ďakujeme " + form.getName());

        return "rezervacia";
    }
}
