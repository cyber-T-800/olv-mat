package org.upece.granko.olvmat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upece.granko.olvmat.entity.AdminEntity;
import org.upece.granko.olvmat.entity.AdminRegistraciaZiadostEntity;
import org.upece.granko.olvmat.entity.enums.AdminRegistraciaZiadostStavEnum;
import org.upece.granko.olvmat.model.AdminDetails;
import org.upece.granko.olvmat.repository.AdminRegistraciaZiadostRepository;
import org.upece.granko.olvmat.repository.AdminRepository;
import org.upece.granko.olvmat.repository.TicketRepository;
import org.upece.granko.olvmat.service.EmailService;
import org.upece.granko.olvmat.service.VytvorenieHeslaSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final EmailService emailService;
    private final AdminRegistraciaZiadostRepository adminRegistraciaZiadostRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final VytvorenieHeslaSession vytvorenieHeslaSession;
    private final TicketRepository ticketRepository;

    @Value("${super.admin.email}")
    private String superAdminEmail;
    @Value("${hostport}")
    private String hostport;

    private final int maxPocetListkov = 200;

    @GetMapping("/admin/login")
    public String getLogin() {

        return "admin/login";
    }


    @GetMapping("/admin")
    public String getAdminPage(ModelMap modelMap) {
        modelMap.put("pocetObsadenych", ticketRepository.countUcastnicke());
        modelMap.put("maxPocet", maxPocetListkov);
        modelMap.put("user", ((AdminDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return "admin/admin";
    }


    @GetMapping("/nejaka-dlha-url-ze-by-nam-tu-randomaci-nechodili")
    public String getRegistracia() {
        return "admin/registracia";
    }

    @PostMapping("/nejaka-dlha-url-ze-by-nam-tu-randomaci-nechodili")
    public String postRegistracia(@Param("email") String email) {
        Map<String, Object> model = new HashMap<>();

        AdminRegistraciaZiadostEntity entity = adminRegistraciaZiadostRepository.save(
                new AdminRegistraciaZiadostEntity(email)
        );

        String url = hostport + "/admin-ziadost/" + entity.getId() + "/potvrdit" + "?sa=" + superAdminEmail + "&em=" + email + "&secret=" + entity.getSecret();

        model.put("email", email);
        model.put("url", url);

        emailService.sendMail(superAdminEmail, "Žiadosť o registráciu admina", "admin-registracia-superadmin", model);
        return "admin/registracia-potvrdenie";
    }

    @GetMapping("/admin-ziadost/{id}/potvrdit")
    public String potvrdenieSuperUserom(
            @PathVariable("id") UUID id,
            @RequestParam(value = "secret") UUID secret,
            @RequestParam(value = "sa") String SAEmail,
            @RequestParam(value = "em") String email
    ) {
        if (!SAEmail.equals(superAdminEmail)) {
            throw new RuntimeException();
        }
        AdminRegistraciaZiadostEntity entity = adminRegistraciaZiadostRepository.findById(id).orElseThrow();
        if (entity.getSecret().equals(secret) && entity.getEmail().equals(email)) {
            if (entity.getStav() == AdminRegistraciaZiadostStavEnum.PODANA) {
                entity.setStav(AdminRegistraciaZiadostStavEnum.POTVRDENA);

                adminRegistraciaZiadostRepository.save(entity);
                String url = hostport + "/admin-ziadost/" + entity.getId() + "/vytvorenie-hesla" + "?em=" + email + "&secret=" + entity.getSecret();

                Map<String, Object> model = new HashMap<>();
                model.put("email", email);
                model.put("url", url);

                emailService.sendMail(email, "Vaša žiadosť bola schválená", "admin-registracia-mail-heslo", model);


                return "admin/registracia-schvalenie";
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
    }

    @GetMapping("/admin-ziadost/{id}/vytvorenie-hesla")
    public String vytvorenieHesla(
            @PathVariable("id") UUID id,
            @RequestParam(value = "secret") UUID secret,
            @RequestParam(value = "em") String email
    ) {
        AdminRegistraciaZiadostEntity entity = adminRegistraciaZiadostRepository.findById(id).orElseThrow();
        if (entity.getSecret().equals(secret) && entity.getEmail().equals(email)) {
            if (entity.getStav() == AdminRegistraciaZiadostStavEnum.POTVRDENA) {
                vytvorenieHeslaSession.setSecret(secret);
                vytvorenieHeslaSession.setEmail(email);

                return "admin/vytvorenie-hesla";
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
    }

    @PostMapping("/admin-ziadost/{id}/vytvorenie-hesla")
    public String vytvorenieUlozit(@PathVariable("id") UUID id, @Param("password") String password) {
        AdminRegistraciaZiadostEntity entity = adminRegistraciaZiadostRepository.findById(id).orElseThrow();
        if (entity.getSecret().equals(vytvorenieHeslaSession.getSecret()) && entity.getEmail().equals(vytvorenieHeslaSession.getEmail())) {
            if (entity.getStav() == AdminRegistraciaZiadostStavEnum.POTVRDENA) {
                AdminEntity admin = new AdminEntity(vytvorenieHeslaSession.getEmail(), passwordEncoder.encode(password));
                adminRepository.save(admin);
                entity.setStav(AdminRegistraciaZiadostStavEnum.VYBAVENA);
                adminRegistraciaZiadostRepository.save(entity);
                return "admin/vytvorenie-uctu-potvrdenie";
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
    }
}
