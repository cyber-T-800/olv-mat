package org.upece.granko.olvmat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.upece.granko.olvmat.entity.enums.AdminRoleEnum;

import java.util.UUID;

@Entity
@Table(name = "admin")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity {
    @Id
    private UUID id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AdminRoleEnum rola;

    public AdminEntity(String email, String password, AdminRoleEnum rola) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.rola = rola;
    }
}
