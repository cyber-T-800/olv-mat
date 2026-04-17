package org.upece.granko.olvmat.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.upece.granko.olvmat.entity.enums.VolunteerStavEnum;

import java.util.UUID;

@Entity
@Table(name = "volunteer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerEntity {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String text;
    private String availability;
    private String services;
    private boolean emailSend;
    @Enumerated(value = EnumType.STRING)
    private VolunteerStavEnum stav;
}
