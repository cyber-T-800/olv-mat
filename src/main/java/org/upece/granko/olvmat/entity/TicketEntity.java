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
@Table(name = "ticket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketEntity {
    @Id
    private UUID id;

    private UUID securityKey;

    private String email;
    private String meno;
    private String stav;

    private TypListkaEnum typListka;

    public TicketEntity(String meno, String email){
        id = UUID.randomUUID();
        securityKey = UUID.randomUUID();

        this.email = email;
        this.meno = meno;
        this.stav = "rezervovany";
        this.typListka = TypListkaEnum.STUDENTSKY;
    }
}
