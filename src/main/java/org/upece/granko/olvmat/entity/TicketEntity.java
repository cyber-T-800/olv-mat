package org.upece.granko.olvmat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.upece.granko.olvmat.entity.enums.StavRezervacieEnum;
import org.upece.granko.olvmat.entity.enums.TypListkaEnum;

import java.util.UUID;

@Entity
@Table(name = "ticket")
@NoArgsConstructor
@Getter
@Setter
public class TicketEntity {
    @Id
    private UUID id;

    private UUID securityKey;

    private String email;
    private String meno;

    @Enumerated(value = EnumType.STRING)
    private TypListkaEnum typListka;

    @Enumerated(value = EnumType.STRING)
    private StavRezervacieEnum stav;

    @JoinColumn(name = "schvalil", referencedColumnName = "id")
    @ManyToOne
    private AdminEntity schvalil;

    public TicketEntity(String meno, String email, TypListkaEnum typListka) {
        id = UUID.randomUUID();
        securityKey = UUID.randomUUID();

        this.email = email;
        this.meno = meno;
        this.stav = StavRezervacieEnum.REZERVOVANY;
        this.typListka = typListka;
    }
}
