package org.upece.granko.olvmat.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.entity.enums.StavRezervacieEnum;
import org.upece.granko.olvmat.model.Ticket;
import org.upece.granko.olvmat.repository.TicketRepository;

import java.util.UUID;

@RestController
@RequestMapping("admin/vstup")
@RequiredArgsConstructor
public class VstupRestController {

    private final TicketRepository ticketRepository;

    @GetMapping("{id}")
    public Ticket nacitajListok(@PathVariable UUID id, HttpServletResponse response) {
        TicketEntity entity = ticketRepository.findById(id).orElse(null);
        if (entity == null) {
            response.setStatus(404);
            return null;
        }
        return new Ticket(entity.getId(), entity.getEmail(), entity.getMeno(), entity.getTypListka(), entity.getStav());
    }

    @PutMapping("{id}/potvrd")
    public void potvrdListok(@PathVariable UUID id, HttpServletResponse response) {
        TicketEntity entity = ticketRepository.findById(id).orElse(null);
        if (entity == null) {
            response.setStatus(404);
            return;
        }
        if (entity.getStav() != StavRezervacieEnum.ZAPLATENY) {
            response.setStatus(400);
        }
        entity.setStav(StavRezervacieEnum.POUZITY);
        ticketRepository.save(entity);
        response.setStatus(200);
    }
}
