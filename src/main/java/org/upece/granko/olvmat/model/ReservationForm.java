package org.upece.granko.olvmat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.upece.granko.olvmat.entity.enums.TypListkaEnum;

@Data
@NoArgsConstructor
public class ReservationForm {
    private String name;
    private String email;
    private TypListkaEnum ticketType;
}
