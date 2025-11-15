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

    public AdminEntity(String email, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
    }
}
