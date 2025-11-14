package org.upece.granko.olvmat.model;

import org.upece.granko.olvmat.entity.enums.StavRezervacieEnum;
import org.upece.granko.olvmat.entity.enums.TypListkaEnum;

import java.util.UUID;

public record Ticket(
        UUID id,
        String email,
        String meno,
        TypListkaEnum typListka,
        StavRezervacieEnum stav
) {
}
