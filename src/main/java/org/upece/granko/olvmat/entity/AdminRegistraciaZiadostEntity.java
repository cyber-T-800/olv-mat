package org.upece.granko.olvmat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.upece.granko.olvmat.entity.enums.AdminRegistraciaZiadostStavEnum;

import java.util.UUID;

@Entity
@Table(name = "admin_registracia_ziadost")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegistraciaZiadostEntity {
    @Id
    private UUID id;
    private UUID secret;

    private String email;
    @Enumerated(EnumType.STRING)
    private AdminRegistraciaZiadostStavEnum stav;

    public AdminRegistraciaZiadostEntity(String email) {
        this.id = UUID.randomUUID();
        this.secret = UUID.randomUUID();
        this.stav = AdminRegistraciaZiadostStavEnum.PODANA;
        this.email = email;
    }
}
