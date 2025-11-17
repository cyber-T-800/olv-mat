package org.upece.granko.olvmat.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.upece.granko.olvmat.entity.AdminEntity;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.entity.enums.StavRezervacieEnum;
import org.upece.granko.olvmat.model.AdminDetails;
import org.upece.granko.olvmat.model.Ticket;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.service.EmailService;
import org.upece.granko.olvmat.utils.QRCodeGenerator;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/tickets")
public class AdminRestController {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @GetMapping()
    public List<Ticket> getTickets() {
        return ticketRepository.findAllExceptZrusene().stream().map(this::ticketFromEntity).toList();
    }

    @PutMapping("/{id}/zaplatenie")
    public void zaplatenieListka(@PathVariable("id") UUID id, HttpServletResponse response) throws Exception {
        Optional<TicketEntity> optEntity = ticketRepository.findById(id);

        if (optEntity.isEmpty()) {
            response.setStatus(404);
            return;
        }
        TicketEntity entity = optEntity.get();

        if (entity.getStav() == StavRezervacieEnum.ZAPLATENY) {
            response.setStatus(400);
            return;
        }

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("meno", entity.getMeno().split(" ")[0]);

        byte[] qrImage = QRCodeGenerator.generateQrPng(entity.getId().toString());

        emailService.sendMail(entity.getEmail(), "Tvoj lístok na OĽV", "potvrdenie-zaplatenie", mailModel, qrImage);

        AdminEntity admin = ((AdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAdminEntity();
        entity.setSchvalil(admin);
        entity.setStav(StavRezervacieEnum.ZAPLATENY);
        ticketRepository.save(entity);

        response.setStatus(200);
    }

    @DeleteMapping("/{id}/zrusenie")
    public void zrusenieListka(@PathVariable("id") UUID id, HttpServletResponse response) {
        Optional<TicketEntity> optEntity = ticketRepository.findById(id);

        if (optEntity.isEmpty()) {
            response.setStatus(404);
            return;
        }
        TicketEntity entity = optEntity.get();

        entity.setStav(StavRezervacieEnum.ZRUSENY);
        ticketRepository.save(entity);

        response.setStatus(200);
    }

    public Ticket ticketFromEntity(TicketEntity entity) {
        return new Ticket(entity.getId(), entity.getEmail(), entity.getMeno(), entity.getTypListka(), entity.getStav());
    }
}
