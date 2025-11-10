package org.upece.granko.olvmat.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
public class UptimeController {
    @GetMapping("healt")
    public UptimeStatus getUp() {
        return new UptimeStatus("UP", LocalDate.now());
    }

    public record UptimeStatus(
            String up,
            LocalDate time
    ) {
    }
}
