package org.upece.granko.olvmat.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.UUID;

@Component
@SessionScope
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VytvorenieHeslaSession {
    private UUID secret;
    private String email;
}
