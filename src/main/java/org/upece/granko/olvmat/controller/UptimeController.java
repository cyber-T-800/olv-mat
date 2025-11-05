package org.upece.granko.olvmat.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.upece.granko.olvmat.entity.TicketEntity;
import org.upece.granko.olvmat.repository.TicketRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class UptimeController {

    private final TicketRepository ticketRepository;

    @GetMapping("healt")
    public UptimeStatus getUp() {
        return new UptimeStatus("UP", LocalDate.now());
    }

    @GetMapping("test")
    public List<TicketEntity> getTest() {
        return ticketRepository.findAll();
    }

    public record UptimeStatus(
            String up,
            LocalDate time
    ) {
    }
}
