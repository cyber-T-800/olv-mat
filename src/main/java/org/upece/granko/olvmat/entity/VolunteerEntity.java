package org.upece.granko.olvmat.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
