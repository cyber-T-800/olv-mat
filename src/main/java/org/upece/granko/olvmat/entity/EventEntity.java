package org.upece.granko.olvmat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "event")
@NoArgsConstructor
@Getter
@Setter
public class EventEntity {
    @Id
    private UUID id;
    private String nazov;
    private String stav;
    private String popis;
}
