package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.model.ReservationForm;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.service.EmailService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    private int maxPocetListkov = 70;
    private int lastListkyPocet = 25;

    @GetMapping("")
    public String rezervacia(ModelMap model) {
        int pocetUcastnickych = ticketRepository.countUcastnicke();
        model.put("percentObsadene", 100.0f * ticketRepository.countUcastnicke() / maxPocetListkov);

        if(maxPocetListkov - pocetUcastnickych <= lastListkyPocet){
            model.put("lastListky", maxPocetListkov - pocetUcastnickych);
        }
        return "rezervacia";
    }

    @PostMapping("")
    public String odosliRezervaciu(ReservationForm form, ModelMap model) {
        if (form.getName() == null || form.getEmail() == null) {
            return "redirect:/";
        }

        TicketEntity entity = ticketRepository.save(new TicketEntity(form.getName(), form.getEmail(), form.getTicketType()));

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("krstneMeno", form.getName().split(" ")[0]);
        mailModel.put("ticketId", entity.getId());
        mailModel.put("securityKey", entity.getSecurityKey());
        emailService.sendMail(form.getEmail(), "Potvrdenie rezervácie listka", "potvrdenie-rezervacie", mailModel);

        model.put("ticketId", entity.getId());
        model.put("securityKey", entity.getSecurityKey());
        return "potvrdenie";
    }

    @GetMapping("/ticket/{id}")
    public String skontrolujRezervaciu(@PathVariable("id") String id, @RequestParam(value = "securityKey", required = false) String securityKey, ModelMap model) {
        if (id == null || id.isBlank() || securityKey == null || securityKey.isBlank()) {
            return "redirect:/";
        }
        try {
            TicketEntity entity = ticketRepository.findById(UUID.fromString(id)).orElseThrow();

            if (!entity.getSecurityKey().equals(UUID.fromString(securityKey))) {
                System.out.println(entity.getSecurityKey());
                System.out.println(UUID.fromString(securityKey));
                throw new RuntimeException();
            }

            model.put("meno", entity.getMeno());
            model.put("mail", entity.getEmail());
            model.put("typ", entity.getTypListka());
            model.put("stav", entity.getStav());

            return "stav-listka";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
